package foodcourt.in.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemRequestDto {
    @NotBlank(message = "name can not be name")
    private String name;

    @NotNull(message = "Restaurant Id can not be null")
    private Long restaurantId;

    @NotNull(message = "Price is Required")
    private Double price;
}
