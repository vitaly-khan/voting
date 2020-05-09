package ru.vitalykhan.voting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vitalykhan.voting.AuthenticatedUser;

import java.util.Objects;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static AuthenticatedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthenticatedUser) ? (AuthenticatedUser) principal : null;
    }

    public static AuthenticatedUser get() {
        AuthenticatedUser user = safeGet();
        Objects.requireNonNull(user, "No authenticated user was found!");
        return user;
    }

    public static int authUserId() {
        return get().getId();
    }

}