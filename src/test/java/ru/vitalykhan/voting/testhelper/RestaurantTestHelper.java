package ru.vitalykhan.voting.testhelper;

import ru.vitalykhan.voting.TestMatcher;
import ru.vitalykhan.voting.model.Restaurant;

import java.util.List;

import static ru.vitalykhan.voting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestHelper {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = new TestMatcher<>(Restaurant.class);

    public static final int RESTAURANT1_ID = START_SEQ + 5;
    public static final int RESTAURANT2_ID = START_SEQ + 6;
    public static final int RESTAURANT3_ID = START_SEQ + 7;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Korean");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Japanese");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Georgian");

    public static final List<Restaurant> SORTED_RESTAURANTS = List.of(RESTAURANT3, RESTAURANT2, RESTAURANT1);

    public static Restaurant getNew() {
        return new Restaurant("New restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Korean Updated");
    }
}
