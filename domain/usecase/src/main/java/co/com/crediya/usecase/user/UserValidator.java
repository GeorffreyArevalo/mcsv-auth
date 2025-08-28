package co.com.crediya.usecase.user;

import co.com.crediya.model.User;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidator {

    private static final String REGEX_VALID_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_ONLY_NUMBERS = "^\\d+$";

    private UserValidator() {}

    public static Mono<User> validateSaveUser(User user) {

        if( !user.getDocument().matches(REGEX_ONLY_NUMBERS) ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_DOCUMENT_MUST_BE_ONLY_NUMBERS.getMessage()));
        }

        if( !user.getEmail().matches(REGEX_VALID_EMAIL) ) {
            return Mono.error(new CrediyaIllegalArgumentException(ExceptionMessages.FIELD_EMAIL_NOT_VALID.getMessage()));
        }

        return Mono.just(user);

    }

}
