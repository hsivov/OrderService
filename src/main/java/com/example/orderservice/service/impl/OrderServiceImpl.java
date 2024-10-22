package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.GameDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.model.dto.UserDTO;
import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.GameService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final GameService gameService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, GameService gameService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        UserDTO customer = userService.getUserById(order.getCustomerId());
        List<GameDTO> boughtGames = gameService.getGamesByIds(order.getBoughtGamesIds());

        return new OrderResponseDTO(order, customer, boughtGames);
    }

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequest) {
        Order order = new Order();
        order.setCustomerId(createOrderRequest.getCustomerId());
        order.setBoughtGamesIds(createOrderRequest.getGameIds());
        order.setTotalPrice(createOrderRequest.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(createOrderRequest.getOrderDate());

        Order savedOrder = orderRepository.save(order);

        UserDTO customer = userService.getUserById(savedOrder.getCustomerId());
        List<GameDTO> boughtGames = gameService.getGamesByIds(savedOrder.getBoughtGamesIds());

        return new OrderResponseDTO(savedOrder, customer, boughtGames);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        UserDTO customer = userService.getUserById(customerId);

        return orders.stream().map(order -> {
            List<GameDTO> boughtGames = gameService.getGamesByIds(order.getBoughtGamesIds());
            return new OrderResponseDTO(order, customer, boughtGames);
        }).toList();
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        UserDTO customer = userService.getUserById(updatedOrder.getCustomerId());
        List<GameDTO> boughtGames = gameService.getGamesByIds(updatedOrder.getBoughtGamesIds());

        return new OrderResponseDTO(updatedOrder, customer, boughtGames);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            UserDTO customer = userService.getUserById(order.getCustomerId());
            List<GameDTO> boughtGames = gameService.getGamesByIds(order.getBoughtGamesIds());
            return new OrderResponseDTO(order, customer, boughtGames);
        }).toList();
    }
}
