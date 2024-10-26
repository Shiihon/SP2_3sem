package app.security.token;

/**
 * Exception thrown when a JWT token cannot be created.
 */
public class TokenCreationException extends Exception {
    /**
     * Constructs a TokenCreationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructs a TokenCreationException with the specified message.
     *
     * @param message the detail message
     */
    public TokenCreationException(String message) {
        super(message);
    }
}

