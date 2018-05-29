package br.com.devdojo.examgenerator.security.filter;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static br.com.devdojo.examgenerator.security.filter.Constants.*;

/**
 * @author William Suane for DevDojo on 10/10/17.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ApplicationUser user = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ZonedDateTime expTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
        String accessType = ((ApplicationUser) authResult.getPrincipal()).getProfessor() != null ? "professor" : "student";
        String token = Jwts.builder()
                .setSubject(((ApplicationUser) authResult.getPrincipal()).getUsername())
                .setExpiration(Date.from(expTimeUTC.toInstant()))
                .claim("accessType", accessType)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        token = TOKEN_PREFIX + token;
        String tokenJson = String.format("{\"token\": %s, \"exp\": %s, \"accessType\": %s}", addQuotes(token), addQuotes(expTimeUTC.toString()), addQuotes(accessType));
        response.getWriter().write(tokenJson);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.addHeader(HEADER_STRING, token);
    }

    private String addQuotes(String value) {
        return new StringBuilder(300).append("\"").append(value).append("\"").toString();
    }
}
