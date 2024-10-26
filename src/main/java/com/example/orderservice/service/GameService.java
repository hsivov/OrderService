package com.example.orderservice.service;

import com.example.orderservice.model.dto.GameDTO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface GameService {
    List<GameDTO> getGamesByIds(Set<Long> gameIds) throws NoSuchAlgorithmException, InvalidKeyException;
}
