package co.com.crediya.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record CheckRolePermissionDTO(

        @NotBlank(message = "is required.")
        String roleCode,

        @NotBlank(message = "is required.")
        String path,

        @NotBlank(message = "is required.")
        String method

) {
}
