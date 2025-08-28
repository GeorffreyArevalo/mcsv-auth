package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.user.CreateUserRequestDTO;
import co.com.crediya.api.dtos.user.UserResponseDTO;
import co.com.crediya.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User createRequestToModel( CreateUserRequestDTO request );

    UserResponseDTO modelToResponse(User model );

}