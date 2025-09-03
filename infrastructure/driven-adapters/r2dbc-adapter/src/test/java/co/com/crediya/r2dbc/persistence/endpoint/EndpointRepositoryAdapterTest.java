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

    @Test
    @DisplayName("Must find endpoints by role code")
    void mustFindEndpointsByRoleCode() {

        String roleCode = "ADMIN";
        when(repository.findByRoleCode(roleCode))
                .thenReturn(Flux.just(endpointEntityOne, endpointEntityTwo));

        when(mapper.map(endpointEntityOne, Endpoint.class)).thenReturn(endpointOne);
        when(mapper.map(endpointEntityTwo, Endpoint.class)).thenReturn(endpointTwo);


        Flux<Endpoint> result = endpointRepositoryAdapter.findByRoleCode(roleCode);


        StepVerifier.create(result)
                .expectNextMatches(e -> e.getPath().equals(endpointOne.getPath()) && e.getMethod().equals(endpointOne.getMethod()))
                .expectNextMatches(e -> e.getPath().equals(endpointTwo.getPath()) && e.getMethod().equals(endpointTwo.getMethod()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return empty when no endpoints are found for role code")
    void mustReturnEmptyWhenNoEndpointsFound() {

        String roleCode = "INVALID";
        when(repository.findByRoleCode(roleCode)).thenReturn(Flux.empty());

        Flux<Endpoint> result = endpointRepositoryAdapter.findByRoleCode(roleCode);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
