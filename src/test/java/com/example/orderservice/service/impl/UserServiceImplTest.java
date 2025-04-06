package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field secretField = UserServiceImpl.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(userService, "test-secret-key");
    }

    @Test
    void testGetUserById() throws NoSuchAlgorithmException, InvalidKeyException {
        UserDTO expected = new UserDTO();
        expected.setFirstName("test-first-name");
        expected.setLastName("test-last-name");
        expected.setId(1L);

        ResponseEntity<UserDTO> mockResponse = new ResponseEntity<>(expected, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserDTO.class)
        )).thenReturn(mockResponse);

        UserDTO actual = userService.getUserById(1L);

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getId(), actual.getId());
    }
}