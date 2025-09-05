package co.com.crediya.r2dbc.persistence.endpoint;

import co.com.crediya.model.Endpoint;
import co.com.crediya.model.gateways.EndpointRepositoryPort;
import co.com.crediya.r2dbc.entities.EndpointEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class EndpointRepositoryAdapter extends ReactiveAdapterOperations<Endpoint, EndpointEntity, Long, EndpointRepository> implements EndpointRepositoryPort {

    public EndpointRepositoryAdapter(EndpointRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Endpoint.class));
    }

    @Override
    public Mono<Boolean> existsEndpointByCodeRoleAndPathAntMethod(String code, String path, String method) {
        return repository.existsEndpointByRoleCodeAndPathAndMethod(code,  path, method);
    }
}
