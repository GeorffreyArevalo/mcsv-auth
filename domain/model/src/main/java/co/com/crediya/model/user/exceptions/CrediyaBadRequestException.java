package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.user.enums.ExceptionStatusCode;

public class CrediyaBadRequestException extends CrediyaException {

    public CrediyaBadRequestException(String message) {
        super( ExceptionStatusCode.BAD_REQUEST, message );
    }


}
