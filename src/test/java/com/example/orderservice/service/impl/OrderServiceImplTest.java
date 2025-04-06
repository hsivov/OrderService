package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.OrderItemDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.entity.OrderItem;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(12L);
        orderItem.setTitle("title");
        orderItem.setPrice(BigDecimal.valueOf(49.99));

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setItems(List.of(orderItem));
        mockOrder.setStatus(OrderStatus.APPROVED);
        mockOrder.setTotalPrice(BigDecimal.valueOf(299.99));
    }

    @Test
    void testGetOrderById() throws NoSuchAlgorithmException, InvalidKeyException {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        OrderResponseDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.APPROVED, result.getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.getTotalPrice());

        verify(orderRepository, times(1)).findById(orderId);
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void testCreateOrder_WhenPaymentMethodIsCreditCard_ShouldReturnApprovedStatus() throws NoSuchAlgorithmException, InvalidKeyException {
        Long orderId = 1L;

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(12L);
        orderItemDTO.setTitle("title");
        orderItemDTO.setPrice(BigDecimal.valueOf(49.99));

        CreateOrderRequestDTO createOrderRequestDTO = new CreateOrderRequestDTO();
        createOrderRequestDTO.setCustomerId(1L);
        createOrderRequestDTO.setOrderItems(List.of(orderItemDTO));
        createOrderRequestDTO.setPaymentMethod("credit-card");

        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        OrderResponseDTO result = orderService.createOrder(createOrderRequestDTO);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.APPROVED, result.getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.getTotalPrice());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void testCreateOrder_WhenPaymentMethodIsNotCreditCard_ShouldReturnPendingStatus() throws NoSuchAlgorithmException, InvalidKeyException {
        Long orderId = 1L;

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(12L);
        orderItemDTO.setTitle("title");
        orderItemDTO.setPrice(BigDecimal.valueOf(49.99));

        CreateOrderRequestDTO createOrderRequestDTO = new CreateOrderRequestDTO();
        createOrderRequestDTO.setCustomerId(1L);
        createOrderRequestDTO.setOrderItems(List.of(orderItemDTO));
        createOrderRequestDTO.setPaymentMethod("bitcoin");

        mockOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        OrderResponseDTO result = orderService.createOrder(createOrderRequestDTO);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.getTotalPrice());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void testGetOrdersByCustomerId() throws NoSuchAlgorithmException, InvalidKeyException {
        Long customerId = 1L;

        List<Order> mockOrders = List.of(mockOrder);

        when(orderRepository.findByCustomerId(customerId)).thenReturn(mockOrders);

        List<OrderResponseDTO> result = orderService.getOrdersByCustomer(customerId);

        assertNotNull(result);
        assertEquals(mockOrders.size(), result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(OrderStatus.APPROVED, result.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.get(0).getTotalPrice());

        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        OrderStatus status = OrderStatus.REJECTED;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        orderService.updateOrder(orderId, status);

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetAllOrders() throws NoSuchAlgorithmException, InvalidKeyException {
        List<Order> mockOrders = List.of(mockOrder);

        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(mockOrders.size(), result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(OrderStatus.APPROVED, result.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.get(0).getTotalPrice());

        verify(orderRepository, times(1)).findAll();
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void testGetPendingOrders() throws NoSuchAlgorithmException, InvalidKeyException {
        mockOrder.setStatus(OrderStatus.PENDING);
        List<Order> mockOrders = List.of(mockOrder);

        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(mockOrders);

        List<OrderResponseDTO> result = orderService.getPendingOrders();

        assertNotNull(result);
        assertEquals(mockOrders.size(), result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(OrderStatus.PENDING, result.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(299.99), result.get(0).getTotalPrice());

        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
        verify(userService, times(1)).getUserById(any());
    }
}