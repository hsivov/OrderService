package com.example.orderservice.service;

import com.example.orderservice.model.dto.UserDTO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserDTO getUserById(Long userId) throws NoSuchAlgorithmException, InvalidKeyException;
}
