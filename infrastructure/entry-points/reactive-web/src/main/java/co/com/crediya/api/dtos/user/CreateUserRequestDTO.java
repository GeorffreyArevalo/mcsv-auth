package co.com.crediya.api.dtos.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequestDTO(

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
        @Min( value = 1, message = "min value is 1")
        @Max( value = 15000000, message = "max value is 15000000")
        BigDecimal basePayment
) {

}
