package foodcourt.in.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteUserDto {
    public DeleteUserDto(String msg)
    {
        this.msg=msg;
    }
    private String msg;
}
