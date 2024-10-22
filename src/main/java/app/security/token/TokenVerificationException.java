package app.security.token;

/**
 * Exception thrown when a JWT token cannot be verified.
 */
public class TokenVerificationException extends Exception {
    /**
     * Constructs a TokenCreationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public TokenVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}

