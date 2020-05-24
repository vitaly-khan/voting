package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;

import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vitalykhan.voting.TestUtil.httpBasicOf;
import static ru.vitalykhan.voting.TestUtil.parentExceptionIs;
import static ru.vitalykhan.voting.controller.VoteController.VOTE_UPDATE_DEADLINE;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.DATE_OF_2020_05_03;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU1_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU5_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.EMPTY_MENU_ID;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.ADMIN1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1_ID;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER2;
import static ru.vitalykhan.voting.testhelper.VoteTestHelper.TODAYS_VOTES;
import static ru.vitalykhan.voting.testhelper.VoteTestHelper.USER2_VOTES;
import static ru.vitalykhan.voting.testhelper.VoteTestHelper.VOTES_OF_2020_05_03;
import static ru.vitalykhan.voting.testhelper.VoteTestHelper.VOTE_MATCHER;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/votes/";

    @Autowired
    private VoteController controller;

    @Test
    void getAllForAuthUser() throws Exception {
        perform(get(REST_URL)
                .with(httpBasicOf(USER2)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.unmarshalAndMatchWith(USER2_VOTES));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(get(REST_URL + "filter?date=" + DATE_OF_2020_05_03)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.unmarshalAndMatchWith(VOTES_OF_2020_05_03));
    }

    @Test
    void getTodays() throws Exception {
        perform(get(REST_URL + "todays")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.unmarshalAndMatchWith(TODAYS_VOTES));
    }

    @Test
    void vote() throws Exception {
        perform(put(REST_URL + "?menuId=" + MENU5_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isOk());
    }

    @Test
    void revote() throws Exception {
        ResultActions resultActions = perform(put(REST_URL + "?menuId=" + MENU5_ID)
                .with(httpBasicOf(USER2)));
        if (LocalTime.now().isAfter(VOTE_UPDATE_DEADLINE)) {
            resultActions.andExpect(status().isUnprocessableEntity())
                    .andExpect(parentExceptionIs(IllegalVoteException.class));
        } else {
            resultActions.andExpect(status().isOk());
        }
    }


    //    Tests for AUTHORIZATION --------------------------------------------------------------------------------------
    @Test
    void getAllForUnauthorizedUser() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllForAdmin() throws Exception {
        perform(get(REST_URL)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllByDateByRegularUser() throws Exception {
        perform(get(REST_URL + "filter?date=" + DATE_OF_2020_05_03)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllByDateByAnonymous() throws Exception {
        perform(get(REST_URL + "filter?date=" + DATE_OF_2020_05_03))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTodaysByRegularUser() throws Exception {
        perform(get(REST_URL + "todays")
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTodaysByAnonymous() throws Exception {
        perform(get(REST_URL + "todays"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void voteByAdmin() throws Exception {
        perform(put(REST_URL + "?menuId=" + MENU5_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void voteByAnonymous() throws Exception {
        perform(put(REST_URL + "?menuId=" + MENU5_ID))
                .andExpect(status().isUnauthorized());
    }


    //    Tests for IllegalVote ----------------------------------------------------------------------------------------
    @Test
    void voteForNotFoundMenu() throws Exception {
        perform(put(REST_URL + "?menuId=" + USER1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(parentExceptionIs(NoSuchElementException.class));
    }

    @Test
    void voteForNotTodaysMenu() throws Exception {
        perform(put(REST_URL + "?menuId=" + MENU1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(parentExceptionIs(IllegalVoteException.class));
    }

    @Test
    void voteForEmptyTodaysMenu() throws Exception {
        perform(put(REST_URL + "?menuId=" + EMPTY_MENU_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(parentExceptionIs(IllegalVoteException.class));
    }
}