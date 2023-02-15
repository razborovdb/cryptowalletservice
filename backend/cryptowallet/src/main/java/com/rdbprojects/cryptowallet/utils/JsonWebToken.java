package com.rdbprojects.cryptowallet.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbprojects.cryptowallet.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class JsonWebToken {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jwt.secret}")
    private String secret;

    public Optional<String> genJsonWebToken(Users createdUsers) {

        try {
            String json = objectMapper.writeValueAsString(createdUsers);
            Map<String, Object> claims = new HashMap<>();
            claims.put("name",createdUsers.getName());
            claims.put("email",createdUsers.getEmail());
            claims.put("role",createdUsers.getRole());
            claims.put("isAdmin",createdUsers.getIsAdmin());
            String jwts = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();

            return Optional.of(jwts);
        } catch (JsonProcessingException e) {
            System.out.println("Error:   " + e.getMessage());
            return Optional.empty();
        }
    }

    private Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Users getUserInformationFromToken(String token) {
        Users user = new Users();
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            user.setName((String) claims.get("name"));
            user.setIsAdmin((Boolean) claims.get("isAdmin"));
            user.setEmail((String) claims.get("email"));
            user.setRole((String) claims.get("role"));
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}
