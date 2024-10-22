package com.example.orderservice.service;

import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO getOrderById(Long orderId);

    OrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequest);

    List<OrderResponseDTO> getOrdersByCustomer(Long customerId);

    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status);

    List<OrderResponseDTO> getAllOrders();
}
