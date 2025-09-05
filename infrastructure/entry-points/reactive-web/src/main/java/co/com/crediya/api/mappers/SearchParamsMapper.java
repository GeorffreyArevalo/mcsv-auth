package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.auth.CheckRolePermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SearchParamsMapper {


    CheckRolePermissionDTO queryParamsToCheckRolePermissionDTO(MultiValueMap<String, String> params);

    default String map(List<String> values) {
        return (values != null && !values.isEmpty()) ? values.getFirst() : null;
    }

    default Integer mapToInt(List<String> values) {
        return (values != null && !values.isEmpty()) ? Integer.parseInt(values.getFirst()) : null;
    }

}
