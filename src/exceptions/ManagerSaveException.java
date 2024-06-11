package exceptions;

/**
 * Исключение для ошибки сохранения задачи
 */
public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message) {
        super(message);
    }
}