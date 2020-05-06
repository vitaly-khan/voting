package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.model.Dish;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.to.DishTo;

public class DishUtil {
    private DishUtil() {
    }

    public static Dish of(DishTo dishTo, Menu menu) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice(), menu);
    }

}
