package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.vitalykhan.voting.TestUtil;
import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.testhelper.RestaurantTestHelper;
import ru.vitalykhan.voting.to.MenuTo;
import ru.vitalykhan.voting.util.MenuUtil;
import ru.vitalykhan.voting.util.exception.ErrorType;
import ru.vitalykhan.voting.util.exception.IllegalOperationException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vitalykhan.voting.TestUtil.detailMessageIs;
import static ru.vitalykhan.voting.TestUtil.errorTypeIs;
import static ru.vitalykhan.voting.TestUtil.httpBasicOf;
import static ru.vitalykhan.voting.TestUtil.parentExceptionIs;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.ALL_TODAYS_MENUS;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.DISABLED_MENU;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.DISABLED_MENU_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.EMPTY_MENU_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.ENABLED_NOT_EMPTY_TODAYS_MENUS;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.ENABLED_TODAYS_MENUS;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU1;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU1_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU4;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU4_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU5_ID;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU_MATCHER;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.TODAY;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getBackdated;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getDuplicateTo;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getJsonWithWrongField;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getNew;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getUpdated;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.ADMIN1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1_ID;
import static ru.vitalykhan.voting.util.exception.ErrorType.BAD_REQUEST;
import static ru.vitalykhan.voting.util.exception.ErrorType.VALIDATION_ERROR;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/menus/";

    @Autowired
    private MenuController controller;

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MENU_MATCHER.unmarshalAndMatchWith(MENU1));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "history?date=" + TODAY)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.unmarshalAndMatchWith(ALL_TODAYS_MENUS));
    }

    @Test
    void getEnabledByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "?date=" + TODAY)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.unmarshalAndMatchWith(ENABLED_TODAYS_MENUS));
    }

    @Test
    void getTodays() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "todays"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.unmarshalAndMatchWith(ENABLED_NOT_EMPTY_TODAYS_MENUS));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + EMPTY_MENU_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        assertThrows(NoSuchElementException.class, () -> controller.getById(EMPTY_MENU_ID));
    }

    @Test
    void enable() throws Exception {
        Menu toBeEnabled = new Menu(DISABLED_MENU);
        Assert.isTrue(!toBeEnabled.isEnabled(), "Menu must be disabled initially");
        toBeEnabled.setEnabled(true);
        perform(MockMvcRequestBuilders.patch(REST_URL + DISABLED_MENU_ID + "?enabled=true")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(controller.getById(DISABLED_MENU_ID), toBeEnabled);
    }

    @Test
    void disable() throws Exception {
        Menu toBeDisabled = new Menu(MENU4);
        Assert.isTrue(toBeDisabled.isEnabled(), "Menu must be enabled initially");
        toBeDisabled.setEnabled(false);
        perform(MockMvcRequestBuilders.patch(REST_URL + MENU4_ID + "?enabled=false")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(controller.getById(MENU4_ID), toBeDisabled);

        //TODO: add cascade on disable test
    }

    @Test
    void create() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(newMenu)))
                .with(httpBasicOf(ADMIN1)));

        Menu created = TestUtil.readFromResultAction(action, Menu.class);
        int newId = created.getId();
        newMenu.setId(newId);

        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(controller.getById(newId), newMenu);
    }

    @Test
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MENU4_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(updated)))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(controller.getById(MENU4_ID), updated);
    }

    //    Tests for AUTHORIZATION --------------------------------------------------------------------------------------
    @Test
    void getByIdByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByIdByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getByDateByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "history?date=" + TODAY)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByDateByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "history?date=" + TODAY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getEnabledByDateByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + DISABLED_MENU_ID + "?enabled=true")
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getEnabledByDateByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + DISABLED_MENU_ID + "?enabled=true"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(getNew())))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createByAnonymousUser() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(getNew()))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU4_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(getUpdated())))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU4_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(getUpdated()))))
                .andExpect(status().isUnauthorized());
    }


    //    Tests for NoSuchElementException------------------------------------------------------------------------------
    @Test
    void getByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER1_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER1_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void enableNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER1_ID + "?enabled=true")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createNotFoundRestaurant() throws Exception {
        MenuTo menuTo = getDuplicateTo();
        menuTo.setRestaurantId(MENU1_ID);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateNotFoundMenu() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.getToFrom(getUpdated())))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateNotFoundRestaurant() throws Exception {
        MenuTo menuTo = getDuplicateTo();
        menuTo.setRestaurantId(MENU1_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(errorTypeIs(ErrorType.DATA_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }


    //    Tests for Enabled Restaurant ---------------------------------------------------------------------------------

    @Test
    void createDisabledRestaurant() throws Exception {
        MenuTo menuTo = getDuplicateTo();
        menuTo.setRestaurantId(RestaurantTestHelper.DISABLED_RESTAURANT_ID);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(parentExceptionIs(IllegalOperationException.class));
    }
    //TODO: add more


    //    Tests for Present or Future Menu -----------------------------------------------------------------------------
    //TODO: add some


    //    Tests for Validation -----------------------------------------------------------------------------------------
    @Test
    void createBackdated() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getBackdated()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR));
    }

    @Test
    void updateBackdated() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getBackdated()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR));
    }

    @Test
    void createWithWrongField() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getJsonWithWrongField()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isBadRequest())
                .andExpect(errorTypeIs(BAD_REQUEST))
                .andExpect(parentExceptionIs(HttpMessageNotReadableException.class));
    }

    @Test
    void updateWithWrongField() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getJsonWithWrongField()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isBadRequest())
                .andExpect(errorTypeIs(BAD_REQUEST))
                .andExpect(parentExceptionIs(HttpMessageNotReadableException.class));
    }


    //    Tests for Database integrity constraint violation ------------------------------------------------------------
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getDuplicateTo()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(GlobalExceptionHandler.DATE_RESTAURANT_MENU_DUPLICATION));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getDuplicateTo()))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(GlobalExceptionHandler.DATE_RESTAURANT_MENU_DUPLICATION));
    }
}