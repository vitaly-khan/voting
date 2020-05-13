package ru.vitalykhan.voting.util.exception;

public class ErrorInfo {
    private final String url;
    private final String type;
    private final String parentExceptionName;
    private final String rootExceptionName;
    private final String[] details;

    public ErrorInfo(StringBuffer url,
                     ErrorType type,
                     String parentExceptionName,
                     String rootExceptionName,
                     String... details) {
        this.url = url.toString();
        this.type = type.getDescription();
        this.parentExceptionName = parentExceptionName;
        this.rootExceptionName = rootExceptionName;
        this.details = details;
    }
}