package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.user.CreateUserRequestDTO;
import co.com.crediya.api.dtos.user.UserResponseDTO;
import co.com.crediya.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    private final CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(
            "Julian",
            "Arevalo",
            "arevalo@gmail.com",
            "10900122",
            "210012312",
            LocalDate.now(),
            "1234",
            "88612512",
            BigDecimal.TEN
    );

    private final User user = User.builder().
            name("Julian")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .document("10900122")
            .phone("210012312")
            .basePayment(BigDecimal.TEN)
            .build();

    @Test
    void testCreateRequestToModel() {
        Mono<User> result = Mono.fromCallable(() -> userMapper.createRequestToModel(createUserRequestDTO, 1L));

        StepVerifier.create(result)
                .expectNextMatches( userResponse ->
                        userResponse.getName().equals(createUserRequestDTO.name())
                        &&  userResponse.getEmail().equals(createUserRequestDTO.email())
                        &&  userResponse.getDocument().equals(createUserRequestDTO.document())
                        &&  userResponse.getPhone().equals(createUserRequestDTO.phone())
                        &&  userResponse.getBasePayment().equals(createUserRequestDTO.basePayment())
                        && userResponse.getLastName().equals(createUserRequestDTO.lastName())
                )
                .verifyComplete();
    }


    @Test
    void testModelToResponse() {
        Mono<UserResponseDTO> result = Mono.fromCallable(() -> userMapper.modelToResponse(user));

        StepVerifier.create(result)
                .expectNextMatches( userResponse ->
                        userResponse.name().equals(user.getName())
                )
                .verifyComplete();
    }

}
