package foodcourt.in.restaurant.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import foodcourt.in.restaurant.dto.OrderItemRequestDto;
import foodcourt.in.restaurant.dto.OrderItemResponseDto;
import foodcourt.in.restaurant.dto.OrderRequestDto;
import foodcourt.in.restaurant.dto.OrderResponseDto;
import foodcourt.in.restaurant.entity.MenuItem;
import foodcourt.in.restaurant.entity.Order;
import foodcourt.in.restaurant.entity.OrderItem;
import foodcourt.in.restaurant.entity.Restaurant;
import foodcourt.in.restaurant.entity.User;
import foodcourt.in.restaurant.enums.OrderStatus;
import foodcourt.in.restaurant.exception.ResourceNotFoundException;
import foodcourt.in.restaurant.repository.MenuItemRepository;
import foodcourt.in.restaurant.repository.OrderRepository;
import foodcourt.in.restaurant.repository.RestaurantRepository;
import foodcourt.in.restaurant.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    OrderService(OrderRepository repository ,
                 RestaurantRepository restaurantRepository,
                 MenuItemRepository menuItemRepository,
                 UserRepository userRepository)
    {
        this.orderRepository = repository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto)
    {
        Order order = new Order();
        Restaurant restaurant = restaurantRepository.
                findById(orderRequestDto.getRestaurantId()).
                orElseThrow(() -> new ResourceNotFoundException("Wrong restaurant id: " + orderRequestDto.getRestaurantId()));

        User user = userRepository.
                findById(orderRequestDto.getUserId()).
                orElseThrow(() -> new ResourceNotFoundException("Wrong user id: " + orderRequestDto.getUserId()));

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderDate(LocalDate.now());

        List<OrderItemRequestDto> listOrderItemDto = orderRequestDto.getList();
        List<OrderItem> listOrderItem = getListOrderItem(listOrderItemDto, order);
        order.setListOrderItem(listOrderItem);

        try {
            order.setOrderStatus(OrderStatus.valueOf(orderRequestDto.getStatus().toUpperCase()));
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Order Status");
        }

        order.setAmt(calcAmt(listOrderItem));

        Order savedOrder = orderRepository.save(order);

        return createOrderResponseDto(savedOrder);
    }

    public OrderResponseDto getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return createOrderResponseDto(order);
    }
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    public OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Wrong restaurant id: " + orderRequestDto.getRestaurantId()));

        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wrong user id: " + orderRequestDto.getUserId()));

        existingOrder.setUser(user);
        existingOrder.setRestaurant(restaurant);

        List<OrderItemRequestDto> listOrderItemDto = orderRequestDto.getList();
        List<OrderItem> listOrderItem = getListOrderItem(listOrderItemDto, existingOrder);

        existingOrder.getListOrderItem().clear();
        existingOrder.getListOrderItem().addAll(listOrderItem);

        try {
            existingOrder.setOrderStatus(OrderStatus.valueOf(orderRequestDto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Order Status");
        }

        existingOrder.setAmt(calcAmt(existingOrder.getListOrderItem()));

        Order updatedOrder = orderRepository.save(existingOrder);
        return createOrderResponseDto(updatedOrder);
    }

    private OrderResponseDto createOrderResponseDto(Order savedOrder) {
        OrderResponseDto responseDto = new OrderResponseDto();

        responseDto.setOrderId(savedOrder.getId());
        responseDto.setTotalAmt(savedOrder.getAmt());
        responseDto.setOrderStatus(savedOrder.getOrderStatus());
        responseDto.setOrderDate(savedOrder.getOrderDate());

        if (savedOrder.getRestaurant() != null) {
            responseDto.setRestaurantName(savedOrder.getRestaurant().getName());
        }
        List<OrderItemResponseDto> itemResponseDtos = new ArrayList<>();

        if (savedOrder.getListOrderItem() != null) {
            for (OrderItem item : savedOrder.getListOrderItem()) {
                OrderItemResponseDto itemDto = new OrderItemResponseDto();

                if (item.getMenuItem() != null) {
                    itemDto.setMenuItemName(item.getMenuItem().getName());
                    itemDto.setPrice(item.getMenuItem().getPrice());
                }
                itemDto.setQuantity(item.getQuantity());

                itemResponseDtos.add(itemDto);
            }
        }

        responseDto.setItems(itemResponseDtos);

        return responseDto;
    }

    List<OrderItem> getListOrderItem(List<OrderItemRequestDto> itemRequestDtos, Order order)
    {
        List<OrderItem> list = new ArrayList<>();
        for(OrderItemRequestDto itemRequestDto : itemRequestDtos)
        {
            OrderItem orderItem = new OrderItem();
            MenuItem menuItem = menuItemRepository.
                    findById(itemRequestDto.getMenuItemId()).
                    orElseThrow(() -> new ResourceNotFoundException("Menu did not exist with id: " + itemRequestDto.getMenuItemId()));

            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequestDto.getQuantity());
            orderItem.setOrder(order);
            list.add(orderItem);
        }
        return list;
    }

    private Double calcAmt(List<OrderItem> listOrderItem) {
        double totalAmt = 0.0;
        for(OrderItem item : listOrderItem) {
            if(item.getMenuItem() != null && item.getMenuItem().getPrice() != null) {
                totalAmt += (item.getQuantity() * item.getMenuItem().getPrice());
            }
        }
        return totalAmt;
    }
}