package co.com.crediya.usecase.auth;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.model.gateways.EndpointRepositoryPort;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.port.PasswordEncoderPort;
import co.com.crediya.port.TokenProviderPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;

    private final TokenProviderPort tokenProviderPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final EndpointRepositoryPort endpointRepositoryPort;

    public Mono<Token> login(String email, String password) {

        return userRepositoryPort.findByEmail(email)
                .switchIfEmpty(
                    Mono.error( new CrediyaUnathorizedException(ExceptionMessages.INVALID_CREDENTIALS.getMessage()))
                )
                .filter(user -> passwordEncoderPort.verify(password, user.getPassword()))
                .switchIfEmpty(
                    Mono.error( new CrediyaUnathorizedException(ExceptionMessages.INVALID_CREDENTIALS.getMessage()))
                )
                .flatMap( user -> roleRepositoryPort.findById(user.getIdRole()) )
                .flatMap( role -> tokenProviderPort.generateAccessToken(email, role.getCode()) );
    }

    public Mono<Boolean> roleHasPermission(String roleCode, String path, String method) {
        return endpointRepositoryPort.existsEndpointByCodeRoleAndPathAntMethod(roleCode, path, method);
    }

}