package ru.vitalykhan.voting.testhelper;

import ru.vitalykhan.voting.TestMatcher;
import ru.vitalykhan.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static ru.vitalykhan.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT1;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT2;
import static ru.vitalykhan.voting.testhelper.RestaurantTestHelper.RESTAURANT3;

public class MenuTestHelper {
    public static TestMatcher<Menu> MENU_MATCHER = new TestMatcher<>(Menu.class, "dishes");

    public static final LocalDate DATE_OF_2020_05_03 = LocalDate.of(2020,5,3);
    public static final LocalDate TODAY = LocalDate.now();

    public static final int MENU1_ID = START_SEQ + 9;
    public static final int MENU2_ID = START_SEQ + 10;
    public static final int MENU3_ID = START_SEQ + 11;
    public static final int MENU4_ID = START_SEQ + 12;
    public static final int MENU5_ID = START_SEQ + 13;

    public static final Menu MENU1 = new Menu(MENU1_ID, DATE_OF_2020_05_03, RESTAURANT1);
    public static final Menu MENU2 = new Menu(MENU2_ID, DATE_OF_2020_05_03, RESTAURANT2);
    public static final Menu MENU3 = new Menu(MENU3_ID, DATE_OF_2020_05_03, RESTAURANT3);
    public static final Menu MENU4 = new Menu(MENU4_ID, TODAY, RESTAURANT1);
    public static final Menu MENU5 = new Menu(MENU5_ID, TODAY, RESTAURANT2);

    public static final List<Menu> MENUS_OF_2020_05_03 = List.of(MENU1, MENU2, MENU3);
    public static final List<Menu> TODAYS_MENUS = List.of(MENU4, MENU5);
}
