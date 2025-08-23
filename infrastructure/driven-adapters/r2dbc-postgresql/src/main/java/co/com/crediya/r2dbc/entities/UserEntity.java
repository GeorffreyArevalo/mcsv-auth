package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEntity {

    @Id
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private BigDecimal basePayment;

}
