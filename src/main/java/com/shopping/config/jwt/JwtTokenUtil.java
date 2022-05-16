package com.shopping.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
@Component
public class JwtTokenUtil {

    private final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public JwtTokenUtil(String secret) {
        this.secret = secret;
    }

    // Get expiration date from jwt token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Get username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    // Check if token has expired
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }


    // Generate token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Generate the token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * ACCESS_TOKEN_VALIDITY_SECONDS))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }
}
