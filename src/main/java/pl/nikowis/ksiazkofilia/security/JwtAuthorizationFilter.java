package pl.nikowis.ksiazkofilia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.nikowis.ksiazkofilia.model.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private String secret;

    public JwtAuthorizationFilter(AuthenticationManager authManager, String secret) {
        super(authManager);
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String jwtToken = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (SecurityConstants.JWT_TOKEN_COOKIE.equals(c.getName())) {
                    jwtToken = c.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String jwtToken) {

        String role, username;
        Long id;
        Boolean active;

        if (isTokenExpired(jwtToken)) {
            return null;
        }

        try {
            Jws<Claims> parsedClaims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            username = parsedClaims.getBody().getSubject();
            role = (String) parsedClaims.getBody().get(SecurityConstants.TOKEN_ROLE_KEY);
            id = ((Integer) parsedClaims.getBody().get(SecurityConstants.TOKEN_ID_KEY)).longValue();
            active = (Boolean) parsedClaims.getBody().get(SecurityConstants.TOKEN_ACTIVE_KEY);

            if (username != null) {
                UserDetailsImpl userDetails = new UserDetailsImpl();
                userDetails.setId(id);
                userDetails.setLogin(username);
                userDetails.setActive(active);
                userDetails.setRole(role);
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
        } catch (ExpiredJwtException exception) {
            LOGGER.warn("Request to parse expired JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            LOGGER.warn("Request to parse unsupported JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (MalformedJwtException exception) {
            LOGGER.warn("Request to parse invalid JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (SignatureException exception) {
            LOGGER.warn("Request to parse JWT with invalid signature : {} failed : {}", jwtToken, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.warn("Request to parse empty or null JWT : {} failed : {}", jwtToken, exception.getMessage());
        }


        return null;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

}
