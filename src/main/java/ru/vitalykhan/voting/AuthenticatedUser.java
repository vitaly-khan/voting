package ru.vitalykhan.voting;

import ru.vitalykhan.voting.model.User;

import java.util.EnumSet;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

//    private UserTo userTo;
    private User user;

    public AuthenticatedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
                true, EnumSet.of(user.getRole()));
        this.user =user;
    }

    public int getId() {
        return user.getId();
    }

//    public void update(UserTo newTo) {
//        userTo = newTo;
//    }
//
//    public UserTo getUserTo() {
//        return userTo;
//    }
//
//    @Override
//    public String toString() {
//        return userTo.toString();
//    }
}