package com.letsellify.logistics.common.restException;

import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(final Exception ex) {
        if (ex instanceof LogisticsRestException) {
            final LogisticsRestException hex = (LogisticsRestException) ex;
            if (hex.getHttpStatus() == HttpStatus.NOT_FOUND || hex.getHttpStatus() == HttpStatus.FORBIDDEN
                || hex.getHttpStatus() == HttpStatus.UNAUTHORIZED || hex.getHttpStatus() == HttpStatus.CONFLICT 
                || hex.getHttpStatus() == HttpStatus.GONE
                ) {

                log.warn("LogisticsAppRestException: Status [" + hex.getHttpStatus() + "] " + ex.getMessage());

            
            } else {
                log.error("LogisticsAppRestException: Status [" + hex.getHttpStatus() + "] " + ex.getMessage(), ex);

            }

            final LogisticsErrorResponse errorResponse = new LogisticsErrorResponse();
            errorResponse.setErrorCode(((LogisticsRestException) ex).getErrorCode());
            errorResponse.setMessage(ex.getMessage());

            return new ResponseEntity<>(errorResponse, hex.getHttpStatus());
        }
        else if (ex instanceof BadCredentialsException || ex instanceof AuthenticationCredentialsNotFoundException || ex instanceof AuthenticationServiceException || ex instanceof DisabledException || ex instanceof DataIntegrityViolationException) {
            final LogisticsErrorResponse errorResponse = new LogisticsErrorResponse();
            errorResponse.setMessage(ex.getMessage());
            switch (ex) {
                case final BadCredentialsException badCredentialsException -> {
                    errorResponse.setErrorCode("400");
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }

                // eg user not found when daoAuthentication tries to authenticate
                case final AuthenticationServiceException authenticationServiceException -> {
                    errorResponse.setErrorCode("401");
                    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
                }
                case final DisabledException disabledException -> {
                    errorResponse.setErrorCode("403");
                    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
                }
                case final DataIntegrityViolationException dataIntegrityViolationException -> {
                    errorResponse.setErrorCode("409");
                    return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
                }

                // eg from within when we dont find a user
                default -> {
                    errorResponse.setErrorCode("404");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
            }

        }
        else {

            if ((!Objects.isNull(ex.getMessage())) && ex.getMessage().startsWith("ServletOutputStream failed to flush")) {
                log.warn("Unhandled Exception: " + ex.getMessage(), ex);
            } else {
                log.error("Unhandled Exception: " + ex.getMessage(), ex);
            }

            final LogisticsErrorResponse errorResponse = new LogisticsErrorResponse();
            errorResponse.setMessage(ex.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //This is called from all other internal spring exceptions
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, @Nullable final Object body, final HttpHeaders headers
            , final HttpStatusCode statusCode, final WebRequest request) {
        log.error("Spring Exception: Status [" + statusCode + "] " + ex.getMessage(), ex);

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    protected ResponseEntity<Object> createResponseEntity(@Nullable final Object body, final HttpHeaders headers,
                                                          final HttpStatusCode statusCode, final WebRequest request) {

        return super.createResponseEntity(body, headers, statusCode, request);
    }
}
