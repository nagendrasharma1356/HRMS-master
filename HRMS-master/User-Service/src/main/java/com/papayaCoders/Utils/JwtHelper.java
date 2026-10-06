package com.papayaCoders.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
@Component
public class JwtHelper {
    private final String SECRET_KEY = "khfrdakadsfuehgfkdnqurhgfksndvgfueahgvkjndsugfhekgfbakudsgfuerbgvuerhgurbgkubgbvkubvgiurbgfvki";  // Store in env/secure config
    private final long EXPIRATION_TIME = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    // get Username form token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }



}
