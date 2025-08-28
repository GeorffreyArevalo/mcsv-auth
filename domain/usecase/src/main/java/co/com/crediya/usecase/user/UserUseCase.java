package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.User;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.model.gateways.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepositoryPort userRepository;


    public Mono<User> saveUser(User user) {

        return userRepository.existByEmailAndDocument(user.getEmail(), user.getDocument())
                .filter( exist -> exist )
                .flatMap( existingUser -> {
                    return Mono.error( new CrediyaBadRequestException( String.format(ExceptionMessages.USER_WITH_EMAIL_EXIST_OR_DOCUMENT.getMessage(),  user.getEmail(), user.getDocument()) ));
                }
                ).switchIfEmpty( UserValidator.validateSaveUser(user) )
                .flatMap( validUser -> userRepository.saveUser(user));

    }


    public Mono<User> findUserByDocument(String document) {
        return userRepository.findByDocument(document)
                .switchIfEmpty( Mono.error(() -> new CrediyaResourceNotFoundException(
                            String.format(ExceptionMessages.USER_WITH_DOCUMENT_NOT_EXIST.getMessage(),  document )
                        )
                ));
    }
}
