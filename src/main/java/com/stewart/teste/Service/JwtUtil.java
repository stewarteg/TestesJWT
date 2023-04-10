package com.stewart.teste.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stewart.teste.Model.JwtPayload;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

@Service
public class JwtUtil {
    private final String secret = "my-secret-key";

    public static void validaSub(Claims claims) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(claims);
        System.out.println(json);
        String username = (String) claims.get("sub");
        System.out.println("o nome eh: "+ username);
    }

    public String generateToken(JwtPayload payload) {
        Claims claims = Jwts.claims().setSubject(payload.getSubject());
        claims.put("roles", payload.getRoles());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1 day

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
