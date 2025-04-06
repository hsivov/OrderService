package com.example.orderservice.service;

import com.example.orderservice.model.dto.OrderItemDTO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface GameService {
    List<OrderItemDTO> getGamesByIds(Set<Long> gameIds) throws NoSuchAlgorithmException, InvalidKeyException;
}
