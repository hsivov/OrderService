package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.UserDTO;
import com.example.orderservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        String url = "http://localhost:8080/api/users/" + userId;
        return restTemplate.getForObject(url, UserDTO.class);
    }
}
