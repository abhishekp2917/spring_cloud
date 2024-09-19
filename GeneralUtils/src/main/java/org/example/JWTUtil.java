package org.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * This class provides methods for validating JWT tokens using a secret key and much more.
 */
public class JWTUtil {

    /**
     * Validates a JWT token against a given secret key.
     *
     * @param token The JWT token to validate.
     * @param secretKey The secret key used to validate the token.
     * @return Returns true if the token is valid, false otherwise.
     *
     * @throws IllegalArgumentException if the secret key is invalid or not compatible.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed.
     */
    public static Boolean validateJWTToken(String token, String secretKey) {
        try {
            // Generate a SecretKey object from the provided secret key string
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            // Parse the JWT token and extract claims (payload)
            Claims claims = Jwts.parser()
                    .verifyWith(key) // Verify the JWT token's signature with the secret key
                    .build()
                    .parseSignedClaims(token) // Parse the JWT and return the payload (claims)
                    .getPayload();

            // If parsing is successful, the token is valid
            return true;
        } catch (Exception ex) {
            // Return false if any exception occurs (e.g., signature validation fails)
            return false;
        }
    }
}
