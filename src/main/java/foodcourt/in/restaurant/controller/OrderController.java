package foodcourt.in.restaurant.controller;

import foodcourt.in.restaurant.dto.OrderRequestDto;
import foodcourt.in.restaurant.dto.OrderResponseDto;
import foodcourt.in.restaurant.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService; // Best practice: make it final

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/book")
    public ResponseEntity<OrderResponseDto> bookOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        OrderResponseDto response = orderService.saveOrder(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findOrder(@PathVariable("id") Long id) {
        OrderResponseDto response = orderService.getOrder(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable("id") Long id,
            @RequestBody @Valid OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.updateOrder(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // Updated
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>("Order Deleted Successfully", HttpStatus.OK);
    }
}