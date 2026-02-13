package com.quotation.util.exception;

/**
 * Exception thrown when a user attempts an action they are not authorized to perform.
 * Results in HTTP 403 Forbidden response.
 */
public class AuthorizationException extends RuntimeException {
    
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
