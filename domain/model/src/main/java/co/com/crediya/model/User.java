package co.com.crediya.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String name;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
    private BigDecimal basePayment;

    private Long idRole;

}
