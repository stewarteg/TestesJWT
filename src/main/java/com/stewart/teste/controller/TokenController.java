package com.stewart.teste.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stewart.teste.Model.JwtPayload;
import com.stewart.teste.Service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token")
    public ResponseEntity<String> generateToken(@RequestHeader("username") String username,
                                                @RequestHeader("roles") String rolesHeader) {
        List<String> roles = Arrays.asList(rolesHeader.split(","));
        JwtPayload payload = new JwtPayload(username, rolesHeader);
        String token = jwtUtil.generateToken(payload);
        return ResponseEntity.ok(token);
    }

    private final String SECRET_KEY = "my-secret-key"; // Chave secreta pré-compartilhada

    @GetMapping("/decode")
    public ResponseEntity<?> decodeJwt(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", ""); // Remove o prefixo "Bearer" do token
            Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
            Claims claims = jws.getBody();
            JwtUtil.validaSub(claims);
            return ResponseEntity.ok(claims);
        } catch (JwtException ex) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

