package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.user.enums.ExceptionStatusCode;

public class CrediyaIllegalArgumentException extends CrediyaException {

    public CrediyaIllegalArgumentException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message);
    }

}
