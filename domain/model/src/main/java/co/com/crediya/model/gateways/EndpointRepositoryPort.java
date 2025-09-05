package co.com.crediya.model.gateways;

import reactor.core.publisher.Mono;

public interface EndpointRepositoryPort {

    Mono<Boolean> existsEndpointByCodeRoleAndPathAntMethod(String code, String path, String method);

}
