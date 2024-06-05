package exceptions;

public class HttpTaskNotFoundException extends RuntimeException {
    public HttpTaskNotFoundException(final String message) {
        super(message);
    }
}