package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.vitalykhan.voting.TestUtil;
import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.util.exception.ErrorType;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vitalykhan.voting.TestUtil.detailMessageIs;
import static ru.vitalykhan.voting.TestUtil.errorTypeIs;
import static ru.vitalykhan.voting.TestUtil.httpBasicOf;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.ALL_RESTAURANTS;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.DISABLED_RESTAURANT;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.DISABLED_RESTAURANT_ID;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.ENABLED_SORTED_RESTAURANTS;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT1;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT1_ID;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT2;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT_MATCHER;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.getNew;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.getUpdated;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.ADMIN1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.USER1_ID;
import static ru.vitalykhan.voting.util.exception.ErrorType.VALIDATION_ERROR;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/";

    @Autowired
    private RestaurantController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.unmarshalAndMatchWith(ENABLED_SORTED_RESTAURANTS));
    }

    @Test
    void getAllWithHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/history")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.unmarshalAndMatchWith(ALL_RESTAURANTS));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.unmarshalAndMatchWith(RESTAURANT1));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISABLED_RESTAURANT_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        assertThrows(NoSuchElementException.class, () -> controller.getById(DISABLED_RESTAURANT_ID));
    }

    @Test
    void enable() throws Exception {
        Restaurant toBeEnabled = new Restaurant(DISABLED_RESTAURANT);
        Assert.isTrue(!toBeEnabled.isEnabled(), "Restaurant must be disabled initially");
        toBeEnabled.setEnabled(true);
        perform(MockMvcRequestBuilders.patch(REST_URL + DISABLED_RESTAURANT_ID + "?enabled=true")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(controller.getById(DISABLED_RESTAURANT_ID), toBeEnabled);
    }

    @Test
    void disable() throws Exception {
        Restaurant toBeDisabled = new Restaurant(RESTAURANT1);
        Assert.isTrue(toBeDisabled.isEnabled(), "Restaurant must be enabled initially");
        toBeDisabled.setEnabled(false);
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT1_ID + "?enabled=false")
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(controller.getById(RESTAURANT1_ID), toBeDisabled);

        //TODO: add cascade on disable test
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant))
                .with(httpBasicOf(ADMIN1)));

        Restaurant created = TestUtil.readFromResultAction(action, Restaurant.class);
        int newId = created.getId();
        newRestaurant.setId(newId);

        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(controller.getById(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(controller.getById(RESTAURANT1_ID), updated);
    }

    //    Tests for AUTHORIZATION --------------------------------------------------------------------------------------
    @Test
    void getByIdByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByIdByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteByIdByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByIdByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void enableByRegularUser() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT1_ID)
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void enableByAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createByRegularUser() throws Exception {
        Restaurant newRestaurant = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createByAnonymous() throws Exception {
        Restaurant newRestaurant = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateByRegularUser() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(httpBasicOf(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateByAnonymous() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
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


    //    Tests for Illegal operation ----------------------------------------------------------------------------------
    @Test
    void deleteByIdWithNestedEntities() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR));
    }


    //    Tests for Validation -----------------------------------------------------------------------------------------
    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorTypeIs(VALIDATION_ERROR));
    }


    //    Tests for Database integrity constraint violation ------------------------------------------------------------
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant withExistedName = new Restaurant(null, RESTAURANT2.getName());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(withExistedName))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(GlobalExceptionHandler.RESTAURANT_NAME_DUPLICATION));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant withExistedName = new Restaurant(RESTAURANT1_ID, RESTAURANT2.getName());

        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(withExistedName))
                .with(httpBasicOf(ADMIN1)))
                .andExpect(status().isConflict())
                .andExpect(errorTypeIs(VALIDATION_ERROR))
                .andExpect(detailMessageIs(GlobalExceptionHandler.RESTAURANT_NAME_DUPLICATION));
    }
}