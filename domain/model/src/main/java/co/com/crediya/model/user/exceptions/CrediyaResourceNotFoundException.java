package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.user.enums.ExceptionStatusCode;

public class CrediyaResourceNotFoundException extends CrediyaException {

    public CrediyaResourceNotFoundException(int statusCode, String message) {
        super(ExceptionStatusCode.NOT_FOUND, message);
    }

}
