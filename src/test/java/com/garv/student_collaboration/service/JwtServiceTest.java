package com.garv.student_collaboration.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    @Test
    void generateToken_shouldContainStudentId(){
        String secret =  "c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==";
        JwtService jwtService = new JwtService(secret,3_600_000L);
        Long studentId = 10L;
        String token = jwtService.generateToken(studentId);
        Long extractedStudentId = jwtService.extractStudentId(token);
        assertEquals(studentId,extractedStudentId);
    }
    @Test
    void isTokenValid_shouldReturnFalseForInvalidToken(){
        String secret =  "c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==";
        JwtService jwtService = new JwtService(secret,3_600_000L);
        boolean valid = jwtService.isTokenValid("not-a-real-jwt");
        assertFalse(valid);
    }
    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken(){
        String secret =  "c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==";
        JwtService jwtService = new JwtService(secret,-1L);
        String token = jwtService.generateToken(10L);
        assertFalse(jwtService.isTokenValid(token));

    }
    @Test
    void isTokenValid_shouldReturnFalseWhenTokenUseDifferentSecret(){
        String firstSecret = "c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==";
        String secondSecret = "YW5vdGhlci10ZXN0LWp3dC1zZWNyZXQta2V5LXRoaXJ0eS10d28=";
        JwtService tokenCreator = new JwtService(firstSecret,3_000_000L);
        JwtService tokenValidator = new JwtService(secondSecret,3_000_000L);
        String token = tokenCreator.generateToken(10L);
        assertFalse(tokenValidator.isTokenValid(token));
    }
    @Test
    void isValidToken_shouldReturnTrueForValidToken(){
        String secret = "c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==";
        JwtService tokenCreator = new JwtService(secret,3_000_000L);
        String token = tokenCreator.generateToken(10L);
        assertTrue(tokenCreator.isTokenValid(token));
    }
}
