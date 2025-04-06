package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.*;
import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.entity.OrderItem;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return createOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequest) {
        Order order = new Order();
        order.setCustomerId(createOrderRequest.getCustomerId());
        order.setTotalPrice(createOrderRequest.getTotalPrice());
        order.setStatus(createOrderRequest.getPaymentMethod().equals("credit-card") ? OrderStatus.APPROVED : OrderStatus.PENDING);
        order.setOrderDate(createOrderRequest.getOrderDate());

        List<OrderItem> items = createOrderRequest.getOrderItems().stream()
                .map(dto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderItemId(dto.getOrderItemId());
                    orderItem.setTitle(dto.getTitle());
                    orderItem.setPrice(dto.getPrice());
                    orderItem.setOrder(order);

                    return orderItem;
                }).toList();

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        return createOrderResponseDTO(savedOrder);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream()
                .map(this::createOrderResponseDTO)
                .toList();
    }

    @Override
    public void updateOrder(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::createOrderResponseDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING).stream()
                .map(this::createOrderResponseDTO)
                .toList();
    }

    private OrderResponseDTO createOrderResponseDTO(Order order) {
        try {
            UserDTO customer = userService.getUserById(order.getCustomerId());

            List<OrderItemDTO> orderItemDTOList = order.getItems().stream()
                    .map(orderItem -> {
                        OrderItemDTO dto = new OrderItemDTO();
                        dto.setOrderItemId(orderItem.getOrderItemId());
                        dto.setTitle(orderItem.getTitle());
                        dto.setPrice(orderItem.getPrice());
                        return dto;
                    }).toList();

            OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
            orderResponseDTO.setId(order.getId());
            orderResponseDTO.setCustomer(customer);
            orderResponseDTO.setBoughtGames(orderItemDTOList);
            orderResponseDTO.setOrderDate(order.getOrderDate());
            orderResponseDTO.setStatus(order.getStatus());
            orderResponseDTO.setTotalPrice(order.getTotalPrice());

            return orderResponseDTO;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to create OrderResponseDTO", e);
        }
    }
}
