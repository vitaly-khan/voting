package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vitalykhan.voting.TestUtil;
import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.User;
import ru.vitalykhan.voting.repository.UserRepository;
import ru.vitalykhan.voting.to.UserTo;
import ru.vitalykhan.voting.util.UserUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vitalykhan.voting.TestUtil.httpBasicOf;
import static ru.vitalykhan.voting.UserTestData.USER1;
import static ru.vitalykhan.voting.UserTestData.USER_MATCHER;

class ProfileControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/profile/";

    @Autowired
    private ProfileController controller;

    @Autowired
    private UserRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.matchJsonWith(USER1));
    }

    @Test
    void disable() {
    }

    @Test
    void register() throws Exception {
        UserTo newUserTo = new UserTo("New User", "newuser@gmail.com", "newpassword");
        User newUser = UserUtil.of(newUserTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = TestUtil.readFromResultAction(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);

        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(repository.findById(newId).orElse(null), newUser);
    }

    @Test
    void update() {
    }

//    Tests for AUTHORIZATION ------------------------------------------------------------------------------------------

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}