package nl.tudelft.sem.template.authentication.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import nl.tudelft.sem.template.authentication.domain.providers.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtTokenGeneratorTests {
    private transient JwtTokenGenerator jwtTokenGenerator;
    private transient TimeProvider timeProvider;
    private transient Instant mockedTime = Instant.parse("2021-12-31T13:25:34.00Z");

    // Secret needs to be at least 512 bits long and base64 encoded
    // In this case: TEST_SECRET_TEST_SECRET_TEST_SECRET_TEST_SECRET_TEST_SECRET12345 -> base64
    private final String secret = "VEVTVF9TRUNSRVRfVEVTVF9TRUNSRVRfVEVTVF9TRUNSRVRfVEVTVF9TRUNSRVRfVEVTVF9TRUNSRVQxMjM0NQ==";

    private String netId = "andy";
    private UserDetails user;

    /**
     * Set up mocks.
     */
    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        timeProvider = mock(TimeProvider.class);
        when(timeProvider.getCurrentTime()).thenReturn(mockedTime);

        jwtTokenGenerator = new JwtTokenGenerator(timeProvider, secret);
        //this.injectSecret(secret);

        user = new User(netId, "someHash", new ArrayList<>());
    }

    @Test
    public void generatedTokenHasCorrectIssuanceDate() {
        // Act
        String token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token);
        assertThat(claims.getIssuedAt()).isEqualTo(mockedTime.toString());
    }

    @Test
    public void generatedTokenHasCorrectExpirationDate() {
        // Act
        String token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token);
        assertThat(claims.getExpiration()).isEqualTo(mockedTime.plus(1, ChronoUnit.DAYS).toString());
    }

    @Test
    public void generatedTokenHasCorrectNetId() {
        // Act
        String token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token);
        assertThat(claims.getSubject()).isEqualTo(netId);
    }

    private Claims getClaims(String token) {
        Key signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        return Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(Integer.MAX_VALUE)
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
