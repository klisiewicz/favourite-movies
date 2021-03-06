package pl.karollisiewicz.cinema.domain.exception;

/**
 * Exception representing unauthorized request.
 */
public class AuthorizationException extends SecurityException {
    public AuthorizationException() {
        super();
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
