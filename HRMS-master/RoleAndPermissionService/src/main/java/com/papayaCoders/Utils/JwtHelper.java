package com.papayaCoders.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {

    private final String SECRET_KEY = "khfrdakadsfuehgfkdnqurhgfksndvgfueahgvkjndsugfhekgfbakudsgfuerbgvuerhgurbgkubgbvkubvgiurbgfvki";  // Store in env/secure config
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // get Username form token
    public String getUsernameFromToken(String Token){
        return Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(Token)
                .getPayload()
                .getSubject();
    }

    // check the Token expiration
    private boolean isTokenExpired(String token){
        Date expiration = Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    // validateToken
    public boolean isTokenValid(String token, String username){
        String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken.equals(username)&&!isTokenExpired(token));
    }
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }
































//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getUsername(String token) {
//        return getClaims(token).getSubject();
//    }
//
//    public String getRole(String token) {
//        return (String) getClaims(token).get("role");
//    }
//
//    public Claims getClaims(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//        String bearer = request.getHeader("Authorization");
//        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
//    }
}
