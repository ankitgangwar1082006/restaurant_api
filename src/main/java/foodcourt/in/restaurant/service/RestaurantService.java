package foodcourt.in.restaurant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import foodcourt.in.restaurant.dto.RestaurantRequestDto;
import foodcourt.in.restaurant.dto.RestaurantResponseDto;
import foodcourt.in.restaurant.entity.Restaurant;
import foodcourt.in.restaurant.exception.ResourceNotFoundException;
import foodcourt.in.restaurant.repository.RestaurantRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    RestaurantService(RestaurantRepository repository){
        this.repository = repository;
    }

    public RestaurantResponseDto createRestaurant(RestaurantRequestDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setLocation(dto.getLocation());
        restaurant.setName(dto.getName());
        restaurant.setStatus(dto.getStatus());
        restaurant.setRating(0.0); // Default rating rule
        Restaurant savedRestaurant = repository.save(restaurant);

        return mapToResponseDto(savedRestaurant);
    }

    public RestaurantResponseDto updateRestaurant(Long id, RestaurantRequestDto dto) {
        Restaurant restaurant = repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Restaurant ID not found: " + id));

        restaurant.setName(dto.getName());
        restaurant.setLocation(dto.getLocation());
        restaurant.setStatus(dto.getStatus());

        Restaurant updatedRestaurant = repository.save(restaurant);

        return mapToResponseDto(updatedRestaurant);
    }

    public RestaurantResponseDto fetchRestaurant(Long id) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant ID not found: " + id));

        return mapToResponseDto(restaurant);
    }


    private RestaurantResponseDto mapToResponseDto(Restaurant restaurant) {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setLocation(restaurant.getLocation());
        dto.setName(restaurant.getName());
        dto.setStatus(restaurant.isStatus());
        dto.setId(restaurant.getId());
        dto.setRating(calcRating(new ArrayList<>()));
        return dto;
    }


    public double calcRating(List<Integer> reviews){
        return 4.2;
    }
}