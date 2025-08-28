package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    FIELD_EMAIL_NOT_VALID("Field email is not valid"),
    FIELD_PAYMENT_OUT_RANGE("Filed basePayment is out of range"),
    FIELD_DOCUMENT_MUST_BE_ONLY_NUMBERS("Document must be only numbers"),
    USER_WITH_EMAIL_EXIST_OR_DOCUMENT("User with email %s or document %s already exists"),
    USER_WITH_DOCUMENT_EXIST("User with document %s already exists"),
    USER_WITH_DOCUMENT_NOT_EXIST("User with document %s doesn't exists");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }


}
