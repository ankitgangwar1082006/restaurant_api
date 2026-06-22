package foodcourt.in.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private String location;
    private Double rating;
    private boolean status;
}