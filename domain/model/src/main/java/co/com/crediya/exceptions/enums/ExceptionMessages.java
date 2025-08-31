package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    USER_WITH_EMAIL_EXIST_OR_DOCUMENT("User with email %s or document %s already exists"),
    USER_WITH_DOCUMENT_NOT_EXIST("User with document %s doesn't exists"),
    INVALID_CREDENTIALS("Credentials are not valid."),
    UNAUTHORIZED_SENT_TOKEN_INVALID("Sent token is invalid.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }


}
