package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import static ru.vitalykhan.voting.controller.GlobalExceptionHandler.E_MAIL_DUPLICATION;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1_ID;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER_MATCHER;
import static ru.vitalykhan.voting.util.exception.ErrorType.VALIDATION_ERROR;

class ProfileControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/profile/";

    @Autowired
    private UserRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.unmarshalAndMatchWith(USER1));
    }

    @Test
    void disable() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/disable")
                .with(httpBasicOf(USER1)))
                .andExpect(status().isNoContent());

        User disabledUser = new User(USER1);
        disabledUser.setEnabled(false);

        USER_MATCHER.assertMatch(repository.findById(USER1_ID).orElse(null), disabledUser);
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
    void update() throws Exception {
        UserTo updatedUserTo = new UserTo("User Updated", "updatedemail@ya.ru", "updatedpassword");

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isNoContent());

        User newUser = UserUtil.of(updatedUserTo);
        newUser.setId(USER1_ID);

        USER_MATCHER.assertMatch(repository.findById(USER1_ID).orElse(null), newUser);
    }

//    Tests for AUTHORIZATION ------------------------------------------------------------------------------------------

    @Test
    void getUnauthenticated() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void disableUnauthenticated() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/disable"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUnauthenticated() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerAuthenticated() throws Exception {
        UserTo newUserTo = new UserTo("New User", "newuser@gmail.com", "newpassword");

        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

//    Tests for INVALID DATA -------------------------------------------------------------------------------------------

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicateEmail() throws Exception {
        UserTo newUserTo = new UserTo("New User", "user2@gmail.com", "newpassword");

        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(E_MAIL_DUPLICATION));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicateEmail() throws Exception {
        UserTo updatedUserTo = new UserTo("Updated User", "user2@gmail.com", "updatedpassword");

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(E_MAIL_DUPLICATION));
    }

    @Test
    void createInvalid() throws Exception {
        UserTo newUserTo = new UserTo("  ", "email@gmail.com", "password");

        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(parentExceptionIs("org.springframework.web.bind.MethodArgumentNotValidException"));
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo updatedUserTo = new UserTo("  ", "email@gmail.com", "password");

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(parentExceptionIs("org.springframework.web.bind.MethodArgumentNotValidException"));
    }
}