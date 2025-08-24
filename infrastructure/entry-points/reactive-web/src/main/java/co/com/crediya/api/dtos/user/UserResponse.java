package co.com.crediya.api.dtos.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponse(String name, String lastName, String email, String document, String phone, LocalDate dateOfBirth, BigDecimal basePayment ) {

}
