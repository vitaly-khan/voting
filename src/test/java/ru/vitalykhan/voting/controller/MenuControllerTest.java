package ru.vitalykhan.voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import ru.vitalykhan.voting.TestUtil;
import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.util.MenuUtil;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vitalykhan.voting.TestUtil.httpBasicOf;
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
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU_MATCHER;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.TODAY;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getNew;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.getUpdated;
import static ru.vitalykhan.voting.testhelper.UserTestHelper.ADMIN1;

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
        perform(MockMvcRequestBuilders.get(REST_URL + "todays")
                .with(httpBasicOf(ADMIN1)))
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
}