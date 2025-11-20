package Ex.Digital.Api.ExdigitalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer idUser;
    private String name;
    private String email;
    private String roleName;
    private Boolean isActive;
    private LocalDateTime createdAt;
}