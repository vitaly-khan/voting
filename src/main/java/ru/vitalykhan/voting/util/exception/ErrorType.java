package ru.vitalykhan.voting.util.exception;

public enum ErrorType {
    APP_ERROR("Application error"),
    DATA_NOT_FOUND("Data Not Found"),
    DATA_ERROR("Data Error"),
    VALIDATION_ERROR("Validation Error"),
    BAD_REQUEST("Bad Request");

    private final String description;

    ErrorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
