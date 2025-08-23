package co.com.crediya.dtos.user;

import java.math.BigDecimal;

public record UserResponse(String name, String lastName, String email, String document, String phone, BigDecimal basePayment ) {

}
