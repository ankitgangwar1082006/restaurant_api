package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.enums.Role;
import lombok.Data;

@Data
public class AuthResponseDto {
    private Long id;
    private Role role;
    private String token;
}
