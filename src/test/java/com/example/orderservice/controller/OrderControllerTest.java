package com.example.orderservice.controller;

import com.example.orderservice.interceptor.HMACInterceptor;
import com.example.orderservice.model.dto.CreateOrderRequestDTO;
import com.example.orderservice.model.dto.OrderItemDTO;
import com.example.orderservice.model.dto.OrderResponseDTO;
import com.example.orderservice.model.dto.UserDTO;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private HMACInterceptor hmacInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        UserDTO mockCustomer = new UserDTO();
        mockCustomer.setId(1L);

        OrderItemDTO mockGame = new OrderItemDTO();
        mockGame.setTitle("Game Title");

        List<OrderItemDTO> mockGames = List.of(mockGame);

        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        orderResponseDTO.setCustomer(mockCustomer);
        orderResponseDTO.setBoughtGames(mockGames);
        orderResponseDTO.setTotalPrice(BigDecimal.valueOf(299.99));
        orderResponseDTO.setStatus(OrderStatus.APPROVED);
        orderResponseDTO.setOrderDate(LocalDateTime.now());
    }

    @Test
    void testCreateOrder() throws Exception {
        CreateOrderRequestDTO mockRequest = new CreateOrderRequestDTO();
        mockRequest.setCustomerId(1L);
        mockRequest.setTotalPrice(BigDecimal.valueOf(49.99));
        mockRequest.setPaymentMethod("credit-card");

        when(orderService.createOrder(any(CreateOrderRequestDTO.class))).thenReturn(orderResponseDTO);
        when(hmacInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.customer.id").value(1))
                .andExpect(jsonPath("$.boughtGames[0].title").value("Game Title"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<OrderResponseDTO> mockOrders = List.of(orderResponseDTO);

        when(orderService.getAllOrders()).thenReturn(mockOrders);
        when(hmacInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("APPROVED"))
                .andExpect(jsonPath("$[0].customer.id").value(1))
                .andExpect(jsonPath("$[0].boughtGames[0].title").value("Game Title"));
    }

    @Test
    void testGetOrderById() throws Exception {
       Long orderId = 1L;

        when(orderService.getOrderById(orderId)).thenReturn(orderResponseDTO);
        when(hmacInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.customer.id").value(1))
                .andExpect(jsonPath("$.boughtGames[0].title").value("Game Title"));
    }

    @Test
    void testGetOrdersByCustomerId() throws Exception {
        Long customerId = 1L;
        List<OrderResponseDTO> mockCustomerOrders = List.of(orderResponseDTO);

        when(orderService.getOrdersByCustomer(customerId)).thenReturn(mockCustomerOrders);
        when(hmacInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockMvc.perform(get("/api/orders/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("APPROVED"))
                .andExpect(jsonPath("$[0].customer.id").value(1))
                .andExpect(jsonPath("$[0].boughtGames[0].title").value("Game Title"));
    }
}