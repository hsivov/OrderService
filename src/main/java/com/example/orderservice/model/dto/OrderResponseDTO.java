package com.example.orderservice.model.dto;

import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private final Long id;
    private final UserDTO customer;
    private final List<GameDTO> boughtGames;
    private final BigDecimal totalPrice;
    private final OrderStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private final LocalDateTime orderDate;

    public OrderResponseDTO(Order order, UserDTO customer, List<GameDTO> boughtGames) {
        this.id = order.getId();
        this.customer = customer;
        this.boughtGames = boughtGames;
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.orderDate = order.getOrderDate();
    }

    public Long getId() {
        return id;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public List<GameDTO> getBoughtGames() {
        return boughtGames;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
