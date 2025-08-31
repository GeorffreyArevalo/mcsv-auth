package co.com.crediya.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDTO(

        @NotBlank(message = "is required.")
        @Pattern( regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "must be valid email.")
        String email,

        @NotBlank(message = "is required.")
        String password
) {
}
