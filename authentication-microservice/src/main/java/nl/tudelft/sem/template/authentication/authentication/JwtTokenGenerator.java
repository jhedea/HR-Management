package nl.tudelft.sem.template.authentication.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.template.authentication.domain.providers.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Generator for JWT tokens.
 */
@Component
public class JwtTokenGenerator {
    /**
     * Time in milliseconds the JWT token is valid for.
     */
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    private final transient Key jwtKey;

    /**
     * Time provider to make testing easier.
     */
    private final transient TimeProvider timeProvider;

    // automatically loads jwt.secret from resources/application.properties
    @Autowired
    public JwtTokenGenerator(TimeProvider timeProvider, @Value("${jwt.secret}") String jwtSecret) {
        this.timeProvider = timeProvider;
        this.jwtKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    /**
     * Generate a JWT token for the provided user.
     *
     * @param userDetails The user details
     * @return the JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(timeProvider.getCurrentTime().toEpochMilli()))
                .setExpiration(new Date(timeProvider.getCurrentTime().toEpochMilli() + JWT_TOKEN_VALIDITY))
                .signWith(jwtKey, SignatureAlgorithm.HS512).compact();
    }
}
