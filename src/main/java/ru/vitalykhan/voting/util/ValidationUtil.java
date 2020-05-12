package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.HasId;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.util.exception.IllegalRequestDataException;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;
import ru.vitalykhan.voting.util.exception.NotFoundException;

import java.time.LocalDate;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkIsNew(HasId object) {
        if (!object.isNew()) {
            throw new IllegalRequestDataException(object + " must be new (id=null)!");
        }
    }

    public static void assureIdConsistency(HasId object, int id) {
        if (object.isNew()) {
            object.setId(id);
        } else if (object.getId() != id) {
            throw new IllegalRequestDataException(object + " must be with id=" + id);
        }
    }

    public static void checkMenuIsTodays(Menu menu, int menuId, LocalDate today) {
        if (menu == null) {
            throw new NotFoundException(String.format("Menu with id=%d wasn't found!", menuId));
        }

        if (!menu.getDate().equals(today)) {
            throw new IllegalVoteException(String.format("Menu with id=%d can't be voted today!", menuId));
        }
    }

    public static void checkFound(boolean found, int restaurantId, Class clazz) {
        if (!found) {
            throw new NotFoundException(String.format(
                    "%s with id=%d wasn't found!",
                    clazz.getSimpleName().replaceFirst("Controller", ""),
                    restaurantId));
        }
    }
}
