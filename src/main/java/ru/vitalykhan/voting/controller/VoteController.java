package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vitalykhan.voting.AuthenticatedUser;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Vote;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.VoteRepository;
import ru.vitalykhan.voting.util.ValidationUtil;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private static final LocalTime VOTE_UPDATE_DEADLINE = LocalTime.of(11, 0);

    private final Logger log = LoggerFactory.getLogger(getClass());

    private VoteRepository voteRepository;
    private MenuRepository menuRepository;

    public VoteController(VoteRepository voteRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
    }

    //No need for ordering by date as business logic implies all votes are saved chronologically and in no other way
    //It's assumed that the user wishes to see only restaurant names and dates (but not dishes) in his history
    @GetMapping
    public List<Vote> getAllForAuthUser(@AuthenticationPrincipal AuthenticatedUser authUser) {
        int userId = authUser.getId();
        log.info("Get all votes of user with id={}", userId);
        return voteRepository.findAllByUserIdWithRestaurants(userId);
    }

    //Get all votes with restaurants for a specific date, so the front-end can process voting results
    @GetMapping("/filter")
    public List<Vote> getAllByDate(@RequestParam @NotNull LocalDate date) {
        return getByDate(date);
    }

    @GetMapping("/todays")
    public List<Vote> getTodays() {
        return getByDate(LocalDate.now());
    }

    private List<Vote> getByDate(LocalDate date) {
        log.info("Get all votes for {}", date);
        return voteRepository.findAllByDateWithRestaurants(date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void vote(@RequestParam int menuId, @AuthenticationPrincipal AuthenticatedUser authUser) {
        Menu menu = menuRepository.findById(menuId).orElse(null);
        LocalDate today = LocalDate.now();

        ValidationUtil.checkIsValidForVoting(menu, menuId, today);

        int userId = authUser.getId();
        Vote oldVote = voteRepository.findByDateAndUserId(today, userId);

        if (oldVote == null) {
            log.info("User with id={} voted for menu with id={} and date={}", userId, menuId, today);
            voteRepository.save(new Vote(today, menu, authUser.getUser()));
        } else if (LocalTime.now().isBefore(VOTE_UPDATE_DEADLINE)) {
            log.info("User with id={} voted again on {}; old choice: menu with id={}, new choice: menu with id={}",
                    userId, today, oldVote.getMenu().getId(), menuId);
            oldVote.setMenu(menu);
        } else {
            throw new IllegalVoteException(String.format(
                    "User with id=%d can't change his vote after %tH:%<tM", userId, VOTE_UPDATE_DEADLINE));
        }
    }
}
