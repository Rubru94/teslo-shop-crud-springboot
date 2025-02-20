package com.teslo.teslo_shop.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private String secretKey = "yourSecretKeyeVHv1eAwgZiPtv9q6nbYBm5ZcZQ4cDc6";

    private Long jwtExpiration = 1000L * 60 * 60 * 1;// 1 hour

    /**
     * <code>ReflectionTestUtils</code> is a Spring-provided utility for accessing
     * and modifying the private fields and methods of a class during unit testing.
     */
    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
    }

    @Test
    public void testGetExpirationTime_ShouldReturnJwtExpirationSettedByValueApplicationProperty() {
        Long expirationTime = jwtService.getExpirationTime();

        assertEquals(expirationTime, jwtExpiration);
        assertEquals(expirationTime, ReflectionTestUtils.getField(jwtService, "jwtExpiration"));
    }

    @Test
    public void testGenerateToken_ShouldReturnStringGeneratedToken() {
        Key signInKey = jwtService.getSignInKey();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key expectedKey = Keys.hmacShaKeyFor(keyBytes);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals(expectedKey, signInKey);
    }

    @Test
    public void testIsTokenValid_ShouldReturnTrueWithValidToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.extractAllClaims(token);
        boolean isTokenExpired = claims.getExpiration().before(new Date());
        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isTokenValid);
        assertFalse(isTokenExpired);
    }

    @Test
    public void testIsTokenValid_ShouldThrowExpiredJwtExceptionWithExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1L);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.extractAllClaims(token);
        });
    }

}
