package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Name can not be Empty")
    private String name;

    @NotBlank(message = "Email can not be Empty")
    @Email(message = "Enter Valid Email")
    private String email;

    @NotBlank(message = "Password can not be Empty")
    @Size(min = 6 , max = 30)
    private String password;

    @NotBlank(message = "Phone can not be Empty")
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    private Role role = Role.USER;
}
