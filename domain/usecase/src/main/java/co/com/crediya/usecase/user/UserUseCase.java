package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.User;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.port.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoderPort;


    public Mono<User> saveUser(User user) {

        return userRepository.existByEmailOrDocument(user.getEmail(), user.getDocument())
                .filter( exists -> !exists )
                .switchIfEmpty(
                    Mono.error(
                        new CrediyaBadRequestException(String.format(ExceptionMessages.USER_WITH_EMAIL_EXIST_OR_DOCUMENT.getMessage(),  user.getEmail(), user.getDocument()))
                    )
                ).then( Mono.just(user))
                .flatMap( currentUser -> {
                    currentUser.setPassword(passwordEncoderPort.encode(currentUser.getPassword()));
                    return userRepository.saveUser(currentUser);
                });


    }


    public Mono<User> findUserByDocument(String document) {
        return userRepository.findByDocument(document)
                .switchIfEmpty( Mono.error(() -> new CrediyaResourceNotFoundException(
                    String.format(ExceptionMessages.USER_WITH_DOCUMENT_NOT_EXIST.getMessage(),  document )
                )));
    }
}
