package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.OrderItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field secretField = GameServiceImpl.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(gameService, "test-secret-key");
    }

    @Test
    void testGetGamesByIds() throws NoSuchAlgorithmException, InvalidKeyException {
        Set<Long> gameIds = Set.of(1L);
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setTitle("Test Game");
        orderItemDTO.setPrice(BigDecimal.valueOf(29.99));

        List<OrderItemDTO> mockGames = List.of(orderItemDTO);

        ResponseEntity<List<OrderItemDTO>> mockResponse = new ResponseEntity<>(mockGames, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<List<OrderItemDTO>>>any()
        )).thenReturn(mockResponse);

        // When: Calling the method
        List<OrderItemDTO> result = gameService.getGamesByIds(gameIds);

        // Then: Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Game", result.get(0).getTitle());
        assertEquals(new BigDecimal("29.99"), result.get(0).getPrice());
    }
}