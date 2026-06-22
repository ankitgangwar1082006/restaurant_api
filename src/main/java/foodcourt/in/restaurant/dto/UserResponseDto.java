package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private String name;
    private String email;

    private String phone;
    private String address;
    private Role role;
}