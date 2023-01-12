package nl.tudelft.sem.gateway.router.services;

import nl.tudelft.sem.gateway.router.config.SecurityConfiguration;
import nl.tudelft.sem.gateway.router.util.IpRangeReactiveAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    private final transient SecurityConfiguration securityConfiguration;

    public SecurityService(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeExchange()
                    // Prevent outsiders from accessing internal endpoints
                    .pathMatchers("/*/internal/**")
                        .access(new IpRangeReactiveAuthorizationManager(securityConfiguration.getInternalRanges()))
                    .pathMatchers("/*/actuator/**")
                        .access(new IpRangeReactiveAuthorizationManager(securityConfiguration.getInternalRanges()))
                .anyExchange().permitAll()
                .and()
                .build();
    }
}
