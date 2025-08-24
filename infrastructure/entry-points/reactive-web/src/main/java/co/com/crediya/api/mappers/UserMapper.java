package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User createRequestToModel( CreateUserRequest request );

    UserResponse modelToResponse(User model );

}