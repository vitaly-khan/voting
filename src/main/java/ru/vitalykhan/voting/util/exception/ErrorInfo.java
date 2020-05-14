package ru.vitalykhan.voting.util.exception;

public class ErrorInfo {
    private final String url;
    private final String type;
    private final String parentException;
    private final String rootException;
    private final String[] details;

    public ErrorInfo(StringBuffer url,
                     ErrorType type,
                     String parentException,
                     String rootException,
                     String... details) {
        this.url = url.toString();
        this.type = type.getDescription();
        this.parentException = parentException;
        this.rootException = rootException;
        this.details = details;
    }
}