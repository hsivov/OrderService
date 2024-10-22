package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.GameDTO;
import com.example.orderservice.service.GameService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public List<GameDTO> getGamesByIds(Set<Long> gameIds) {
        String url = "http://localhost:8080/api/games";

        RestTemplate restTemplate = new RestTemplate();

        // Create the HTTP entity to send the request body
        HttpEntity<Set<Long>> request = new HttpEntity<>(gameIds);

        // Use ParameterizedTypeReference to specify the response type
        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // Return the list of GameDTO
        return response.getBody();
    }
}
