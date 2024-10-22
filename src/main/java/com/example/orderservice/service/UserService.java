package com.example.orderservice.service;

import com.example.orderservice.model.dto.UserDTO;

public interface UserService {
    UserDTO getUserById(Long userId);
}
