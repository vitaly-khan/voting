package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.model.Role;
import ru.vitalykhan.voting.model.User;
import ru.vitalykhan.voting.to.UserTo;

import java.time.Instant;

public final class UserUtil {
    private UserUtil() {
    }

    public static User of(UserTo userTo) {
        return new User(userTo.getId(), userTo.getName(), userTo.getEmail(), userTo.getPassword(),
                Role.USER, Instant.now(), true);
    }

}
