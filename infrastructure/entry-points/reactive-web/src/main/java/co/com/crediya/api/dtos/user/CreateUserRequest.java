package co.com.crediya.api.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank(message = "is required.")
        String name,

        @NotBlank(message = "is required.")
        String lastName,

        @NotBlank(message = "is required.")
        String email,

        @NotBlank(message = "is required")
        String document,

        String phone,
        LocalDate dateOfBirth,

        @NotNull(message = "is required.")
        BigDecimal basePayment
) {

}
