package foodcourt.in.restaurant.dto;

import foodcourt.in.restaurant.enums.OrderStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long orderId;
    private Double totalAmt;
    private OrderStatus orderStatus;
    private LocalDate orderDate;

    private String restaurantName;

    private List<OrderItemResponseDto> items;
}