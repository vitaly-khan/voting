package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.HasId;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.util.exception.IllegalRequestDataException;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;
import ru.vitalykhan.voting.util.exception.NotFoundException;

import java.time.LocalDate;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkIsNew(HasId object) {
        if (!object.isNew()) {
            throw new IllegalRequestDataException(
                    object.getClass().getSimpleName().replaceFirst("To", "") + " id must be null!");
        }
    }

    public static void assureIdConsistency(HasId object, int id) {
        if (object.isNew()) {
            object.setId(id);
        } else if (object.getId() != id) {
            throw new IllegalRequestDataException(String.format(
                    "%s must be with id=%d!",
                    object.getClass().getSimpleName().replaceFirst("To", ""),
                    id));
        }
    }

    public static void checkIsTodays(Menu menu, int menuId, LocalDate today) {
        if (menu == null) {
            throw new NotFoundException(String.format("Unable to find Menu with id %d", menuId));
        }

        if (!menu.getDate().equals(today)) {
            throw new IllegalVoteException(String.format("Menu with id %d can't be voted today", menuId));
        }
    }

    public static void checkFound(boolean found, int restaurantId, Class clazz) {
        if (!found) {
            throw new NotFoundException(String.format(
                    "Unable to find %s with id %d",
                    clazz.getSimpleName().replaceFirst("Controller", ""),
                    restaurantId));
        }
    }
}
