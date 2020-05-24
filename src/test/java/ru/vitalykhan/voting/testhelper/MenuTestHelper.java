package ru.vitalykhan.voting.testhelper;

import ru.vitalykhan.voting.TestMatcher;
import ru.vitalykhan.voting.model.Menu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static ru.vitalykhan.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT1;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT2;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT_WITH_NO_TODAYS_MENUS;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT4;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT5;

public class MenuTestHelper {
    public static TestMatcher<Menu> MENU_MATCHER = new TestMatcher<>(Menu.class, "dishes");

    public static final LocalDate DATE_OF_2020_05_03 = LocalDate.of(2020,5,3);
    public static final LocalDate TODAY = LocalDate.now();

    public static final int MENU1_ID = START_SEQ + 11;
    public static final int MENU2_ID = START_SEQ + 12;
    public static final int MENU3_ID = START_SEQ + 13;
    public static final int MENU4_ID = START_SEQ + 14;
    public static final int MENU5_ID = START_SEQ + 15;
    public static final int EMPTY_MENU_ID = START_SEQ + 16;
    public static final int DISABLED_MENU_ID = START_SEQ + 17;

    public static final Menu MENU1 = new Menu(MENU1_ID, DATE_OF_2020_05_03, RESTAURANT1);
    public static final Menu MENU2 = new Menu(MENU2_ID, DATE_OF_2020_05_03, RESTAURANT2);
    public static final Menu MENU3 = new Menu(MENU3_ID, DATE_OF_2020_05_03, RESTAURANT_WITH_NO_TODAYS_MENUS);
    public static final Menu MENU4 = new Menu(MENU4_ID, TODAY, RESTAURANT1);
    public static final Menu MENU5 = new Menu(MENU5_ID, TODAY, RESTAURANT2);
    public static final Menu EMPTY_MENU = new Menu(EMPTY_MENU_ID, TODAY, RESTAURANT4);
    public static final Menu DISABLED_MENU = new Menu(DISABLED_MENU_ID, TODAY, RESTAURANT5, false);

    public static final List<Menu> ALL_TODAYS_MENUS = List.of(MENU4, MENU5, EMPTY_MENU, DISABLED_MENU);
    public static final List<Menu> ENABLED_TODAYS_MENUS = List.of(MENU4, MENU5, EMPTY_MENU);
    public static final List<Menu> ENABLED_NOT_EMPTY_TODAYS_MENUS = List.of(MENU5, MENU4);

    public static Menu getNew() {
        return new Menu(null, TODAY, RESTAURANT_WITH_NO_TODAYS_MENUS);
    }

    public static Menu getUpdated() {
        return new Menu(MENU4_ID, TODAY.plus(1, ChronoUnit.DAYS), RESTAURANT_WITH_NO_TODAYS_MENUS);
    }
}
