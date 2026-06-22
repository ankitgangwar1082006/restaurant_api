package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.entity.Restaurant;
import lombok.Data;

@Data
public class MenuItemResponseDto {
    private String imgUrl;
    private Long id;
    private Double price;
    private Long restaurantId;
    private String name;
}
