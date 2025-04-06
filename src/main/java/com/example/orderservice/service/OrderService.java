package com.example.orderservice.service;

import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.model.enums.OrderStatus;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface OrderService {

    OrderResponseDTO getOrderById(Long orderId) throws NoSuchAlgorithmException, InvalidKeyException;

    OrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequest) throws NoSuchAlgorithmException, InvalidKeyException;

    List<OrderResponseDTO> getOrdersByCustomer(Long customerId) throws NoSuchAlgorithmException, InvalidKeyException;

    void updateOrder(Long orderId, OrderStatus status);

    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getPendingOrders();
}
