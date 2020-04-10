package pl.nikowis.ksiazkofilia.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pl.nikowis.ksiazkofilia.exception.UsernameAlreadyExistsException;
import pl.nikowis.ksiazkofilia.model.ApiError;
import pl.nikowis.ksiazkofilia.model.ApiErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        LOGGER.warn("Authentication failure ", ex);
        HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;
        String exceptionName = ex.getClass().getSimpleName();
        ApiError err = new ApiError(UsernameAlreadyExistsException.LOGIN_FIELD, messageSource.getMessage(exceptionName, null, LocaleContextHolder.getLocale()));
        ApiErrorResponse apiError = new ApiErrorResponse(responseStatus, Collections.singletonList(err));
        response.setStatus(responseStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));
    }
}
