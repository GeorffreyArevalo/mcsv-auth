package co.com.crediya.r2dbc.persistence.endpoint;

import co.com.crediya.model.Endpoint;
import co.com.crediya.r2dbc.entities.EndpointEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EndpointRepositoryAdapterTest {

    @InjectMocks
    EndpointRepositoryAdapter endpointRepositoryAdapter;

    @Mock
    EndpointRepository repository;

    @Mock
    ObjectMapper mapper;

    private Endpoint endpointOne;
    private Endpoint endpointTwo;
    private EndpointEntity endpointEntityOne;
    private EndpointEntity endpointEntityTwo;

    @BeforeEach
    void setUp() {
        endpointOne = Endpoint.builder()
                .path("/api/users")
                .method("GET")
                .build();

        endpointTwo = Endpoint.builder()
                .path("/api/roles")
                .method("POST")
                .build();

        endpointEntityOne = EndpointEntity.builder()
                .id(1L)
                .path("/api/users")
                .method("GET")
                .build();

        endpointEntityTwo = EndpointEntity.builder()
                .id(2L)
                .path("/api/roles")
                .method("POST")
                .build();
    }


}
