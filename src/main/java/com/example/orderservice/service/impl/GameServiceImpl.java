package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.GameDTO;
import com.example.orderservice.service.GameService;
import com.example.orderservice.util.HMACUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    @Value("${app.api.url}")
    private String appUrl;

    @Value("${app.api-key}")
    private String apiKey;

    @Value("${app.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    public GameServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<GameDTO> getGamesByIds(Set<Long> gameIds) throws NoSuchAlgorithmException, InvalidKeyException {
        String url = appUrl + "/games";
        String endpoint = "/api/games";

        String method = "POST";

        String timestamp = LocalTime.now().toString();

        String payload = method + endpoint + timestamp;

        String signature = HMACUtil.generateHMAC(payload, secret);

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("X-Signature", signature);
        headers.set("X-Timestamp", timestamp);

        // Create the HTTP entity to send the request body
        HttpEntity<Set<Long>> request = new HttpEntity<>(gameIds, headers);

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
