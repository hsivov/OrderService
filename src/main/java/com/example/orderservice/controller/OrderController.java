package com.example.orderservice.controller;

import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO) throws NoSuchAlgorithmException, InvalidKeyException {
        OrderResponseDTO order = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
