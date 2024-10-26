package com.example.orderservice.service.impl;

import com.example.orderservice.model.dto.UserDTO;
import com.example.orderservice.service.UserService;
import com.example.orderservice.util.HMACUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;

@Service
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    @Value("${app.api.url}")
    private String appUrl;

    @Value("${app.api-key}")
    private String apiKey;

    @Value("${app.secret}")
    private String secret;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDTO getUserById(Long userId) throws NoSuchAlgorithmException, InvalidKeyException {
        String url = appUrl + "/users/" + userId;
        String endpoint = "/api/users/" + userId;

        String method = "GET";

        String timestamp = LocalTime.now().toString();

        String payload = method + endpoint + timestamp;

        String signature = HMACUtil.generateHMAC(payload, secret);

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("X-Signature", signature);
        headers.set("X-Timestamp", timestamp);

        // Create an HttpEntity with the headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Make the request with headers
        ResponseEntity<UserDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDTO.class);

        // Return the body of the response
        return response.getBody();
    }
}
