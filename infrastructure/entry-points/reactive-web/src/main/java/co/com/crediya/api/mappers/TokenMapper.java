package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.auth.TokenResponseDTO;
import co.com.crediya.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TokenMapper {

    TokenResponseDTO modelToResponse(Token token);

}
