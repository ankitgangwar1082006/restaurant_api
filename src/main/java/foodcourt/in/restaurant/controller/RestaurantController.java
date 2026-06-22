package foodcourt.in.restaurant.controller;

import foodcourt.in.restaurant.dto.RestaurantRequestDto;
import foodcourt.in.restaurant.dto.RestaurantResponseDto;
import foodcourt.in.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService service;
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestBody @Valid RestaurantRequestDto dto) {
        RestaurantResponseDto response = service.createRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurant(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.fetchRestaurant(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @PathVariable("id") Long id,
            @RequestBody @Valid RestaurantRequestDto dto) {
        return ResponseEntity.ok(service.updateRestaurant(id, dto));
    }


}