package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.vitalykhan.voting.util.ExceptionUtil;
import ru.vitalykhan.voting.util.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static ru.vitalykhan.voting.util.exception.ErrorType.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String E_MAIL_DUPLICATION = "Such e-mail already exists!";
    public static final String DATE_RESTAURANT_MENU_DUPLICATION = "A menu on this date for this restaurant exists already!";
    public static final String DISH_NAME_MENU_DUPLICATION = "This menu contains a dish with the same name already!";
    public static final String DATE_USER_VOTE_DUPLICATION = "User can't have 2 votes on the same date!";
    public static final String RESTAURANT_NAME_DUPLICATION = "Restaurant with the same name exists already!";

    private static final Map<String, String> UNIQUE_CONSTRAINTS = Map.of(
            "users_unique_email_idx", E_MAIL_DUPLICATION,
            "menu_unique_date_restaurant_id_idx", DATE_RESTAURANT_MENU_DUPLICATION,
            "dish_unique_menu_id_name_idx", DISH_NAME_MENU_DUPLICATION,
            "vote_unique_date_user_id", DATE_USER_VOTE_DUPLICATION,
            "integrity constraint violation: unique constraint or index violation; sys_ct_10255 table: restaurant",
            RESTAURANT_NAME_DUPLICATION);

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  //422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    //MethodArgumentTypeMismatchException is thrown in case of URI: voting/restaurants/sometext
    //HttpMessageNotReadableException is thrown in case of wrong JSON body {"wrongName":"Italian"}
    //HttpRequestMethodNotSupportedException is thrown in case of URI: DELETE: voting/restaurants
    //MissingServletRequestParameterException is thrown in case of URI: voting/menus
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler({IllegalRequestDataException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class/*,
            //TODO: something wrong with these 2 exceptions hadling! It won't work if uncomment!
            MissingServletRequestParameterException.class*//*,
            HttpRequestMethodNotSupportedException.class*/})
    public ErrorInfo badRequestError(HttpServletRequest req, RuntimeException e) {
        return logAndGetErrorInfo(req, e, false, BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalVoteException.class})
    public ErrorInfo illegalRequestDataAndVoteError(HttpServletRequest req, RuntimeException e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    //MethodArgumentTypeMismatchException.class is thrown in case of database unique indices violation.
    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo dbUniqueIndicesViolation(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ExceptionUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : UNIQUE_CONSTRAINTS.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, entry.getValue());
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    //MethodArgumentNotValidException is thrown in case of e.g. creating menu for expired date (Bean Validation)
    //or in case of missing required field in JSON body.
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest req, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        String[] details = result.getFieldErrors().stream()
                .map(FieldError::getField)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   //500
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException,
                                         ErrorType errorType, String... details) {
        Throwable rootCause = ExceptionUtil.getRootCause(e);

        if (logException) {
            log.error(errorType.getDescription() + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType.getDescription(), req.getRequestURL(), e);
        }

        return new ErrorInfo(req.getRequestURL(),
                errorType,
                e.getClass().getName(),
                rootCause.getClass().getName(),
                details.length != 0 ? details : new String[]{rootCause.getMessage()});
    }
}