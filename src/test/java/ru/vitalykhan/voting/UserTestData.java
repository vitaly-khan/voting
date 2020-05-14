package ru.vitalykhan.voting;

import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.Role;
import ru.vitalykhan.voting.model.User;

import static ru.vitalykhan.voting.model.AbstractEntityWithId.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER = new TestMatcher<>(User.class, "password", "registered");

    public static final int USER1_ID = START_SEQ + 1;
    public static final int USER2_ID = START_SEQ;
    public static final int USER3_ID = START_SEQ + 4;
    public static final int ADMIN1_ID = START_SEQ + 3;
    public static final int ADMIN2_ID = START_SEQ + 2;

    public static final User USER1 = new User(USER1_ID, "User 1", "user1@gmail.com", "password1", Role.USER);
    public static final User USER2 = new User(USER2_ID, "User 2", "user2@gmail.com", "password2", Role.USER);
    public static final User USER3 = new User(USER3_ID, "User 3", "user3@gmail.com", "password3", Role.USER);
    public static final User[] USERS = {USER1, USER2, USER3};

    public static final User ADMIN1 = new User(ADMIN1_ID, "Admin 1", "admin1@gmail.com", "admin1", Role.ADMIN);
    public static final User ADMIN2 = new User(ADMIN2_ID, "Admin 2", "admin2@gmail.com", "admin2", Role.ADMIN);
    public static final User[] ADMINS = new User[]{ADMIN1, ADMIN2};

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }

    public static User getNew() {
        return new User(null, "New User", "new@gmail.com", "newPassword", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(USER1);
        updated.setName("User 1 Updated");
        updated.setEmail("user1updated@gmail.com");
        updated.setPassword("updatedPassword");

        return updated;
    }
}
