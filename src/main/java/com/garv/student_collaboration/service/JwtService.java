package com.garv.student_collaboration.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") long expirationMs) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }
    public String generateToken(Long studentId){
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(studentId.toString())
                .issuedAt(now)
                .expiration(expirationTime)
                .signWith(signingKey)
                .compact();
    }
    public Long extractStudentId(String token){
        String subject = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.valueOf(subject);
    }
    public boolean isTokenValid(String token){
        try{
            extractStudentId(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException exception){
            return false;
        }
    }
}
