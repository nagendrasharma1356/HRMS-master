package com.papayaCoder.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.el.parser.Token;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {

    private final String SECRET_KEY = "khfrdakadsfuehgfkdnqurhgfksndvgfueahgvkjndsugfhekgfbakudsgfuerbgvuerhgurbgkubgbvkubvgiurbgfvki";  // Store in env/secure config
    private final long EXPIRATION_TIME = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


    // generate Token
    public String generateToken(String username , String role){
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

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
}