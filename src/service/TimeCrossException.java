package service;

public class TimeCrossException extends RuntimeException {

    //исключение для пересечения задач по времени
    public TimeCrossException(final String message) {
        super(message);
    }
}