package exceptions;

public class ManagerSaveException extends RuntimeException {

    //исключение для ошибки сохранения задачи
    public ManagerSaveException(final String message) {
        super(message);
    }
}