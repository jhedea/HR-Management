package nl.tudelft.sem.contract.microservice.config;

import nl.tudelft.sem.contract.microservice.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.contract.microservice.authentication.JwtRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Web security config.
 */
@Configuration
public class RequestAuthenticationConfig extends WebSecurityConfigurerAdapter {
    private final transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final transient JwtRequestFilter jwtRequestFilter;

    public RequestAuthenticationConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                       JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/internal/**").permitAll() // XXX: This should be secured somehow (maybe IP whitelist)
                .antMatchers("/actuator/**").permitAll() // XXX: As above, needs to be properly secured
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
