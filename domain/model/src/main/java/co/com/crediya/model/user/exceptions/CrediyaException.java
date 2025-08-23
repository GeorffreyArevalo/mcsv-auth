package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.user.enums.ExceptionStatusCode;

public class CrediyaException extends RuntimeException {

    private ExceptionStatusCode statusCode;

    public CrediyaException(ExceptionStatusCode statusCode, String message ) {
        super(message);
        this.statusCode = statusCode;
    }

    public ExceptionStatusCode getStatusCode() {
        return statusCode;
    }

}
