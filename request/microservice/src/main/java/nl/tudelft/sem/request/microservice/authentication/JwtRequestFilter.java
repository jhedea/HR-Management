package nl.tudelft.sem.request.microservice.authentication;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Request filter for JWT security.
 * <p>
 * The request filter is called once for each request and makes it possible to modify the request
 * before it reaches the application. If an authorization header is present in the request,
 * the filter will validate it and authenticate the token.
 * </p>
 */
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private static boolean flag = false;
    private static String token = "";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";
    public static final String AUTHORIZATION_AUTH_SCHEME = "Bearer";

    private final transient JwtTokenVerifier jwtTokenVerifier;

    @Autowired
    public JwtRequestFilter(JwtTokenVerifier jwtTokenVerifier) {
        this.jwtTokenVerifier = jwtTokenVerifier;
    }


    protected String helper(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null) {
            return authorizationHeader;
        } else {
            throw new NullPointerException();
        }
    }

    protected void doFilterInternalHelper(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = helper(request);
        String[] directives;
        if (authorizationHeader != null) {
            directives = authorizationHeader.split(" ");
        } else {
            throw new NullPointerException();
        }
        flag = false;
        token = "";
        if (directives.length == 2 && directives[0].equals(AUTHORIZATION_AUTH_SCHEME)) {
            token = directives[1];
            flag = true;
        }
    }



    /**
     * This filter will verify and authenticate a JWT token if a valid authorization header is set.
     *
     * @param request     The current request we are handling.
     * @param response    The current response we are building.
     * @param filterChain The next link in the filter chain.
     * @throws ServletException Exception.
     * @throws IOException      Exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        request.getHeader(AUTHORIZATION_HEADER);
        try {
            doFilterInternalHelper(request, response);
            if (flag && jwtTokenVerifier.validateToken(token)) {
                String netId = jwtTokenVerifier.getNetIdFromToken(token);
                var authenticationToken = new UsernamePasswordAuthenticationToken(netId,
                        null, new ArrayList<>() // no credentials and no authorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.debug("Invalid token", e);
        }
        filterChain.doFilter(request, response);
    }
}
