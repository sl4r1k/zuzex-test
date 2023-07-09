package com.zuzex.testtask.security;

import com.zuzex.testtask.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtToken {
    private final String secret;

    public JwtToken(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generate(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put("roles", roles);
        Date issued = new Date();
        Date expired = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getUsername())
            .setIssuedAt(issued)
            .setExpiration(expired)
            .signWith(SignatureAlgorithm.HS256, this.secret)
            .compact();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
            .setSigningKey(this.secret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
