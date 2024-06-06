package exceptions;

public class InMemoryTaskNotFoundException extends RuntimeException{
    public InMemoryTaskNotFoundException(final String message) {
        super(message);
    }
}