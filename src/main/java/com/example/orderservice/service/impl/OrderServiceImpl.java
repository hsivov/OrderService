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
    public OrderResponseDTO getOrderById(Long orderId) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return getOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = new Order();
        order.setCustomerId(createOrderRequest.getCustomerId());
        order.setTotalPrice(createOrderRequest.getTotalPrice());
        order.setStatus(createOrderRequest.getPaymentMethod().equals("credit-card") ? OrderStatus.APPROVED : OrderStatus.PENDING);
        order.setOrderDate(createOrderRequest.getOrderDate());

        List<OrderItem> items = createOrderRequest.getOrderItems().entrySet().stream()
                .map(entry -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setTitle(entry.getKey());
                    orderItem.setPrice(entry.getValue());
                    orderItem.setOrder(order);

                    return orderItem;
                }).toList();

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        return getOrderResponseDTO(savedOrder);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream().map(order -> {
            try {
                return getOrderResponseDTO(order);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return getOrderResponseDTO(updatedOrder);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    try {
                        return getOrderResponseDTO(order);
                    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private OrderResponseDTO getOrderResponseDTO(Order order) throws NoSuchAlgorithmException, InvalidKeyException {
        UserDTO customer = userService.getUserById(order.getCustomerId());

        List<OrderItemDTO> orderItemDTOList = order.getItems().stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setTitle(orderItem.getTitle());
                    orderItemDTO.setPrice(orderItem.getPrice());
                    return orderItemDTO;
                }).toList();

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setCustomer(customer);
        orderResponseDTO.setBoughtGames(orderItemDTOList);
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setTotalPrice(order.getTotalPrice());

        return orderResponseDTO;
    }
}
