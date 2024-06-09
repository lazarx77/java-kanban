package exceptions;

public class TaskNotFoundException extends RuntimeException {

    //исключение для несуществующих задач
    public TaskNotFoundException(final String message) {
        super(message);
    }
}