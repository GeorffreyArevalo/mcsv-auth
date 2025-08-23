package co.com.crediya.enums;

public enum ExceptionMessages {

    FIELD_NAME_REQUIRED("Field name is required"),
    FIELD_LAST_NAME_REQUIRED("Field lastName is required"),
    FIELD_EMAIL_NOT_VALID("Field email is not valid"),
    PAYMENT_OUT_RANGE("Filed basePayment is out of range"),
    USER_WITH_EMAIL_EXIST("User with email %s already exists");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
