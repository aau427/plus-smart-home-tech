package andrey.exception;

public class UnknownEventException extends RuntimeException {
    public UnknownEventException(String message) {
        super(message);
    }
}
