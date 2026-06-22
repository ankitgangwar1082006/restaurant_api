package foodcourt.in.restaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Email(message = "valid Email Required")
    private String email;

    @NotBlank(message = "pswd cant be empty")
    @NotNull(message = "pswd cant be empty")
    private String password;
}
