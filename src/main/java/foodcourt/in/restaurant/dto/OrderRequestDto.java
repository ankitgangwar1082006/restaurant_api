package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequestDto> list = new ArrayList<>();

    private String status= OrderStatus.valueOf("PLACED").toString();
}
