package exceptions;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(final String message) {
        super(message);
    }
}