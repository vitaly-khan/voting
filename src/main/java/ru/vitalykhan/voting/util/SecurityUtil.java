package ru.vitalykhan.voting.util;

public class SecurityUtil {

    private SecurityUtil() {
    }

//    public static AuthorizedUser safeGet() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) {
//            return null;
//        }
//        Object principal = auth.getPrincipal();
//        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
//    }
//
//    public static AuthorizedUser get() {
//        AuthorizedUser user = safeGet();
//        requireNonNull(user, "No authorized user found");
//        return user;
//    }

    public static int authUserId() {
        return 100001;
//        return get().getUserTo().id();
    }

//    public static int authUserCaloriesPerDay() {
//        return get().getUserTo().getCaloriesPerDay();
//    }
}