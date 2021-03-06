package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.HasId;
import ru.vitalykhan.voting.controller.DishController;
import ru.vitalykhan.voting.controller.MenuController;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.util.exception.IllegalOperationException;
import ru.vitalykhan.voting.util.exception.IllegalRequestDataException;
import ru.vitalykhan.voting.util.exception.IllegalVoteException;

import java.time.LocalDate;
import java.util.NoSuchElementException;

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

    public static void checkNestedEntityNotExist(boolean notExists, int restaurantId,
                                                 String parentEntityName, String childEntityName) {
        if (!notExists) {
            throw new IllegalOperationException(String.format(
                    "Unable to delete the %s with id %d while it contains at least one %s",
                    parentEntityName, restaurantId, childEntityName));
        }
    }

    public static void checkIsPresentOrFuture(Menu menu) {
        if (menu.getDate().isBefore(LocalDate.now())) {
            throw new IllegalOperationException(String.format(
                    "Operation can't be performed because the affected menu (id %d) is expired", menu.getId()));
        }
    }

    public static void checkIsEnabled(boolean enabled, int parentId, String parentEntityName) {
        if (!enabled) {
            throw new IllegalOperationException(String.format(
                    "Operation can't be performed because the %s with id %d is disabled",
                    parentEntityName, parentId));
        }
    }


    public static void checkIsFound(boolean found) {
        if (!found) {
            throw new NoSuchElementException();
        }
    }
}
