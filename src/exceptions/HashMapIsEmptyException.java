package exceptions;

public class HashMapIsEmptyException extends RuntimeException {
    public HashMapIsEmptyException(final String message) {
        super(message);
    }
}