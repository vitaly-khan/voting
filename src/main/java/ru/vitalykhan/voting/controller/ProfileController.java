package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.model.User;
import ru.vitalykhan.voting.repository.UserRepository;
import ru.vitalykhan.voting.to.UserTo;
import ru.vitalykhan.voting.util.SecurityUtil;
import ru.vitalykhan.voting.util.UserUtil;
import ru.vitalykhan.voting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public User get() {
        log.info("Get current user, id={}", SecurityUtil.authUserId());
        return SecurityUtil.getUser();
    }

    @PutMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable() {
        log.info("Disable user with id={}", SecurityUtil.authUserId());
        User user = SecurityUtil.getUser();
        user.setEnabled(false);
        userRepository.save(user);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("Create new user from UserTo {}", userTo);
        User user = UserUtil.of(userTo);
        User newUser = userRepository.save(passwordAndEmailProcessing(user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(newUser);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo) {
        int authId = SecurityUtil.authUserId();
        log.info("Update user with id={} as {}", authId, userTo);
        ValidationUtil.assureIdConsistency(userTo, authId);

        User user = UserUtil.of(userTo);
        userRepository.save(passwordAndEmailProcessing(user));
    }

    private User passwordAndEmailProcessing(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
