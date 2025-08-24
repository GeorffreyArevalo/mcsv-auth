package co.com.crediya.usecase.user;

import co.com.crediya.model.User;
import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidator {

    private static final BigDecimal MIN_VALUE_BASE_PAYMENT = new BigDecimal(0);
    private static final BigDecimal MAX_VALUE_BASE_PAYMENT = new BigDecimal(15000000);
    private static final String REGEX_VALID_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_ONLY_NUMBERS = "^\\d+$";

    public static Mono<User> validateSaveUser(User user) {

        if( user.getName() == null || user.getName().isBlank() ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_NAME_REQUIRED.getMessage()));
        }

        if( user.getLastName() == null || user.getLastName().isBlank() ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_LAST_NAME_REQUIRED.getMessage()));
        }

        if( user.getDocument() == null || !user.getDocument().matches(REGEX_ONLY_NUMBERS) ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_DOCUMENT_MUST_BE_ONLY_NUMBERS.getMessage()));
        }

        if( user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().matches(REGEX_VALID_EMAIL) ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_EMAIL_NOT_VALID.getMessage()));
        }

        if( user.getBasePayment() == null || user.getBasePayment().compareTo(MIN_VALUE_BASE_PAYMENT) <= 0 || user.getBasePayment().compareTo(MAX_VALUE_BASE_PAYMENT) > 0 ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_PAYMENT_OUT_RANGE.getMessage()));
        }

        return Mono.just(user);

    }

}
