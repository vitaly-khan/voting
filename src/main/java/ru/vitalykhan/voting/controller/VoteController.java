package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Vote;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.UserRepository;
import ru.vitalykhan.voting.repository.VoteRepository;
import ru.vitalykhan.voting.util.SecurityUtil;
import ru.vitalykhan.voting.util.ValidationUtil;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private static final LocalTime UPDATE_VOTE_DEADLINE = LocalTime.of(11, 0);

    private final Logger log = LoggerFactory.getLogger(getClass());

    private VoteRepository voteRepository;
    private MenuRepository menuRepository;

    public VoteController(VoteRepository voteRepository, MenuRepository menuRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<Vote> getAllForAuthUser() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes of user with id={}", userId);
        return voteRepository.findAllByUserId(userId);
    }

    @GetMapping("/filter")
    public List<Vote> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get all votes for date {}", date);
        return voteRepository.findAllByDate(date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void vote(@RequestParam int menuId) {
        Menu menu = menuRepository.findByIDLazily(menuId);
        LocalDate today = LocalDate.now();

        ValidationUtil.checkMenuIsTodays(menu, menuId, today);

        Vote newVote = new Vote(today, menu, SecurityUtil.get().getUser());
        int userId = SecurityUtil.authUserId();

        Vote oldVote = voteRepository.findByDateAndUserId(today, userId);
        if (oldVote == null) {
            log.info("User with id={} voted for menu with id={} and date={}", userId, menuId, today);
            voteRepository.save(newVote);
        } else if (LocalTime.now().isBefore(UPDATE_VOTE_DEADLINE)) {
            log.info("User with id={} voted again on {}; old choice: menu with id={}, new choice: menu with id={}",
                    userId, today, oldVote.getMenu().getId(), menuId);
            oldVote.setMenu(menu);
        } else {
            throw new IllegalVoteException(String.format(
                    "User with id=%d can't change his vote after %tH:%<tM", userId, UPDATE_VOTE_DEADLINE));
        }
    }
}
