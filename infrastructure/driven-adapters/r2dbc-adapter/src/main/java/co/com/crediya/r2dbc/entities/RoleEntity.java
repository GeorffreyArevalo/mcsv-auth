package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RoleEntity {

    @Id
    private Long id;
    private String code;
    private String name;
    private String description;

}
