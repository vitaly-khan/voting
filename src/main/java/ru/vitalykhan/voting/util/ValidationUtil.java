package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.HasId;
import ru.vitalykhan.voting.controller.DishController;
import ru.vitalykhan.voting.controller.MenuController;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.util.exception.IllegalOperationException;
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

    public static void checkIsValidForVoting(Menu menu, int menuId, LocalDate today) {
        checkIsFound(menu != null, menuId, MenuController.ENTITY_NAME);

        if (!menu.getDate().equals(today)) {
            throw new IllegalVoteException(String.format(
                    "The %s with id %d can't be voted today as it's out-of-date", MenuController.ENTITY_NAME, menuId));
        }

        if (menu.getDishes().isEmpty()) {
            throw new IllegalVoteException(String.format(
                    "The %s with id %d can't be voted now as it contains no %s items",
                    MenuController.ENTITY_NAME, menuId, DishController.ENTITY_NAME));
        }
    }

    public static void checkIsFound(boolean found, int restaurantId, String entityName) {
        if (!found) {
            throw new NotFoundException(String.format(
                    "Unable to find the %s with id %d", entityName, restaurantId));
        }
    }

    public static void checkNestedEntityNotExists(boolean notExists, int restaurantId,
                                                  String childEntityName, String parentEntityName) {
        if (!notExists) {
            throw new IllegalOperationException(String.format(
                    "Unable to delete the %s with id %d while it contains at least one %s",
                    parentEntityName, restaurantId, childEntityName));
        }
    }

    public static void checkIsEnabled(boolean enabled, int parentId, String parentEntityName) {
        if (!enabled) {
            throw new IllegalOperationException(String.format(
                    "Operation can't be performed because the %s with id %d is disabled",
                    parentEntityName, parentId));
        }

    }
}
