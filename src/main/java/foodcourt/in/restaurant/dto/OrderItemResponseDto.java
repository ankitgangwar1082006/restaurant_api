package foodcourt.in.restaurant.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private String menuItemName;
    private Double price;
    private Integer quantity;
}