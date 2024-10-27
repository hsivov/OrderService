package com.example.orderservice.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class CreateOrderRequestDTO {
    private Long customerId;
    private Set<Long> gameIds;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String paymentMethod;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Set<Long> getGameIds() {
        return gameIds;
    }

    public void setGameIds(Set<Long> gameIds) {
        this.gameIds = gameIds;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
