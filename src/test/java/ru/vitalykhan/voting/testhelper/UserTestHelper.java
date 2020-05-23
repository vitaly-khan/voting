package ru.vitalykhan.voting.testhelper;

import ru.vitalykhan.voting.TestMatcher;
import ru.vitalykhan.voting.model.Role;
import ru.vitalykhan.voting.model.User;

import java.util.List;

import static ru.vitalykhan.voting.model.AbstractBaseEntity.START_SEQ;


//The utility class name 'UserTestData' doesn't match '[A-Z][a-zA-Z0-9]+(Utils?|Helper|Constants)'
//by Codacy
public class UserTestHelper {
    public static TestMatcher<User> USER_MATCHER = new TestMatcher<>(User.class, "password", "registered");

    public static final int USER1_ID = START_SEQ + 1;
    public static final int USER2_ID = START_SEQ;
    public static final int USER3_ID = START_SEQ + 4;
    public static final int ADMIN1_ID = START_SEQ + 3;

    public static final User USER1 = new User(USER1_ID, "User 1", "user1@gmail.com", "password1", Role.USER);
    public static final User USER2 = new User(USER2_ID, "User 2", "user2@gmail.com", "password2", Role.USER);
    public static final User USER3 = new User(USER3_ID, "User 3", "user3@gmail.com", "password3", Role.USER);

    public static final User ADMIN1 = new User(ADMIN1_ID, "Admin 1", "admin1@gmail.com", "admin1", Role.ADMIN);
}
