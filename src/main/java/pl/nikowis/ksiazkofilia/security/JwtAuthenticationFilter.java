package pl.nikowis.ksiazkofilia.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.nikowis.ksiazkofilia.model.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    private String secret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret, AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            JwtLoginRequest creds = new ObjectMapper()
                    .readValue(req.getInputStream(), JwtLoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLogin(),
                            creds.getPassword())
            );
        } catch (IOException e) {
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            LOGGER.warn("Incorrect authentication attempt", e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.TOKEN_ROLE_KEY, user.getRole());
        claims.put(SecurityConstants.TOKEN_ID_KEY, user.getId());
        final String token = Jwts.builder().setClaims(claims).setSubject(user.getLogin()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

        Cookie tokenCookie = new Cookie(SecurityConstants.JWT_TOKEN_COOKIE, token);
        tokenCookie.setMaxAge(SecurityConstants.JWT_TOKEN_VALIDITY);
        tokenCookie.setHttpOnly(true);
        res.addCookie(tokenCookie);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(new ObjectMapper().writeValueAsString(new JwtLoginResponse(user.getId(), user.getLogin())));
    }
}
