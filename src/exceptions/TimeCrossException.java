package exceptions;

/**
 * Исключение для пересечения задач по времени
 */

public class TimeCrossException extends RuntimeException {

    public TimeCrossException(final String message) {
        super(message);
    }
}