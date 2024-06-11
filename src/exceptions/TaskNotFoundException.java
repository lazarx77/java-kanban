package exceptions;

/**
 * Исключение для несуществующих задач
 */

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(final String message) {
        super(message);
    }
}