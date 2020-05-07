package ru.vitalykhan.voting.util;

import ru.vitalykhan.voting.HasId;
import ru.vitalykhan.voting.util.exception.IllegalRequestDataException;

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
}
