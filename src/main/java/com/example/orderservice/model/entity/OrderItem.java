package com.example.orderservice.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order-items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long orderItemId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long productId) {
        this.orderItemId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
