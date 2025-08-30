package co.com.crediya.api.dtos.user;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequestDTO(

        @NotBlank(message = "is required.")
        String name,

        @NotBlank(message = "is required.")
        String lastName,

        @NotBlank(message = "is required.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "is not valid email"
        )
        String email,

        @NotBlank(message = "is required")
        @Pattern(
                regexp = "^\\d+$",
                message = "must be only numbers"
        )
        String document,

        String phone,
        LocalDate dateOfBirth,

        @NotNull(message = "is required.")
        @Min( value = 1, message = "min value is 1")
        @Max( value = 15000000, message = "max value is 15000000")
        BigDecimal basePayment
) {

}
