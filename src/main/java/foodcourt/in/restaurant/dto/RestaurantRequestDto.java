package foodcourt.in.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRequestDto {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Restaurant location is required")
    private String location;

    @NotNull(message = "Restaurant status can not be null")
    private Boolean status;
}
