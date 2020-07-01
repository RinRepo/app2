
package com.lessons.utilities;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Value("${development.mode}")
    private boolean developmentMode;

    /************************************************************************
     * handleException()
     *
     * This is the Global Exception Handler
     ************************************************************************/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception aException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Log the error *and* stack trace
        if (null != request) {
            logger.error("Exception raised from call to " + request.getRequestURI(), aException);
        } else {
            logger.error("Exception raised from null request.", aException);
        }

        // Return a ResponseEntity with media type as text_plain so that the front-end does not convert this to a JSON map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        if (developmentMode) {
            // I am in developer mode so send the *real* error message to the front-end
            return new ResponseEntity<>(aException.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // I am in production mode so send a *generic* error message to the front-end
            return new ResponseEntity<>("Something went wrong. Please contact an administrator.", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}