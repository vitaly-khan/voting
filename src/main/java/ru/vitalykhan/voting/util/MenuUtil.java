package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.to.MenuTo;

public final class MenuUtil {
    private MenuUtil() {
    }

    public static MenuTo getToFrom(Menu menu) {
        return new MenuTo(menu.getDate(), menu.getRestaurant().getId());
    }
}
