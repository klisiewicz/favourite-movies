package pl.karollisiewicz.movie.domain.exception;

/**
 * Exception representing communication issues with the data source.
 */
public class CommunicationException extends RuntimeException {
    public CommunicationException() {
        super();
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
