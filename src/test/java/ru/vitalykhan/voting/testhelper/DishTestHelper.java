package ru.vitalykhan.voting.testhelper;

import ru.vitalykhan.voting.TestMatcher;
import ru.vitalykhan.voting.model.Dish;

import static ru.vitalykhan.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU1;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU2;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU3;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU4;
import static ru.vitalykhan.voting.testhelper.MenuTestHelper.MENU5;

public class DishTestHelper {
    public static TestMatcher<Dish> DISH_MATCHER = new TestMatcher<>(Dish.class);

    public static final int DISH1_ID = START_SEQ + 14;
    public static final int DISH2_ID = START_SEQ + 15;
    public static final int DISH3_ID = START_SEQ + 16;
    public static final int DISH4_ID = START_SEQ + 17;
    public static final int DISH5_ID = START_SEQ + 18;
    public static final int DISH6_ID = START_SEQ + 19;
    public static final int DISH7_ID = START_SEQ + 20;
    public static final int DISH8_ID = START_SEQ + 21;
    public static final int DISH9_ID = START_SEQ + 22;
    public static final int DISH10_ID = START_SEQ + 23;
    public static final int DISH11_ID = START_SEQ + 24;
    public static final int DISH12_ID = START_SEQ + 25;
    public static final int DISH13_ID = START_SEQ + 26;

    public static final Dish DISH1 = new Dish(DISH1_ID, "Korean Dish 1", 20_000, MENU1);
    public static final Dish DISH2 = new Dish(DISH2_ID, "Korean Dish 3", 15_000, MENU1);
    public static final Dish DISH3 = new Dish(DISH3_ID, "Korean Dish 2", 35_000, MENU1);
    public static final Dish DISH4 = new Dish(DISH4_ID, "Japanese Dish 1", 35_000, MENU2);
    public static final Dish DISH5 = new Dish(DISH5_ID, "Georgian Dish 2", 13_000, MENU3);
    public static final Dish DISH6 = new Dish(DISH6_ID, "Georgian Dish 1", 30_000, MENU3);
    public static final Dish DISH7 = new Dish(DISH7_ID, "Korean Dish 1", 20_000, MENU4);
    public static final Dish DISH8 = new Dish(DISH8_ID, "Korean Dish 5", 30_000, MENU4);
    public static final Dish DISH9 = new Dish(DISH9_ID, "Korean Dish 4", 40_000, MENU4);
    public static final Dish DISH10 = new Dish(DISH10_ID, "Japanese Dish 2", 75_000, MENU5);
    public static final Dish DISH11 = new Dish(DISH11_ID, "Japanese Dish 3", 55_000, MENU5);
    public static final Dish DISH12 = new Dish(DISH12_ID, "Japanese Dish 5", 42_000, MENU5);
    public static final Dish DISH13 = new Dish(DISH13_ID, "Japanese Dish 4", 30_000, MENU5);


//    public static Dish getNew() {
//        return new Dish("New dish");
//    }
//
//    public static Dish getUpdated() {
//        return new Dish(DISH1_ID, "Updated");
//    }
}
