package com.example.orderservice.service;

import com.example.orderservice.model.dto.GameDTO;

import java.util.List;
import java.util.Set;

public interface GameService {
    List<GameDTO> getGamesByIds(Set<Long> gameIds);
}
