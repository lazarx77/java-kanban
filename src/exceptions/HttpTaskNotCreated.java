package exceptions;

public class HttpTaskNotCreated extends RuntimeException {
    public HttpTaskNotCreated(final String message) {
        super(message);
    }
}
