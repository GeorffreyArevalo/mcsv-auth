package co.com.crediya.api.dtos.user;

import java.math.BigDecimal;

public record CreateUserRequest(String name, String lastName, String email, String document, String phone, BigDecimal basePayment ) {

}
