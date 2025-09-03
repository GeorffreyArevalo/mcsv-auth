package co.com.crediya.r2dbc.persistence.endpoint;


import co.com.crediya.model.Endpoint;
import co.com.crediya.model.Role;
import co.com.crediya.model.gateways.EndpointRepositoryPort;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import co.com.crediya.r2dbc.entities.EndpointEntity;
import co.com.crediya.r2dbc.entities.RoleEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbc.persistence.role.RoleRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EndpointRepositoryAdapter extends ReactiveAdapterOperations<Endpoint, EndpointEntity, Long, EndpointRepository> implements EndpointRepositoryPort {

    public EndpointRepositoryAdapter(EndpointRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Endpoint.class));
    }

    @Override
    public Flux<Endpoint> findByRoleCode(String code) {
        return repository.findByRoleCode(code).map( super::toEntity );
    }
}
