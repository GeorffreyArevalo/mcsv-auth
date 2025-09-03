package co.com.crediya.model.gateways;

import co.com.crediya.model.Endpoint;
import reactor.core.publisher.Flux;

public interface EndpointRepositoryPort {

    Flux<Endpoint> findByRoleCode(String code);

}
