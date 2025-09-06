package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.auth.CheckRolePermissionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class SearchParamsMapperTest {

    private final SearchParamsMapper mapper = Mappers.getMapper(SearchParamsMapper.class);

    private MultiValueMap<String, String> params;

    @BeforeEach
    void setUp() {
        params = new LinkedMultiValueMap<>();
        params.add("roleCode", "CLIENT");
        params.add("method", "GET");
    }

    @Test
    void testQueryParamsToCheckRolePermissionDTO() {
        Mono<CheckRolePermissionDTO> result = Mono.fromCallable(() -> mapper.queryParamsToCheckRolePermissionDTO(params));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.roleCode().equals("CLIENT") &&
                                dto.method().equals("GET")
                )
                .verifyComplete();
    }

    @Test
    void testMapWithValues() {
        List<String> values = List.of("ADMIN");

        Mono<String> result = Mono.fromCallable(() -> mapper.map(values));

        StepVerifier.create(result)
                .expectNext("ADMIN")
                .verifyComplete();
    }



    @Test
    void testMapToIntWithValues() {
        List<String> values = List.of("42");

        Mono<Integer> result = Mono.fromCallable(() -> mapper.mapToInt(values));

        StepVerifier.create(result)
                .expectNext(42)
                .verifyComplete();
    }


}
