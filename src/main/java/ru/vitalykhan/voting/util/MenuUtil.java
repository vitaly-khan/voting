package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.To.MenuTo;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Restaurant;

public class MenuUtil {
    private MenuUtil() {}

    public static Menu getMenuOf(MenuTo menuTo, Restaurant restaurant) {
        return new Menu(menuTo.getId(), menuTo.getDate(), restaurant, null);
    }
}
