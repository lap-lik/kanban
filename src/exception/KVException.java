package exception;

public class KVException extends RuntimeException{
    public KVException(String message) {
        super(message);
    }

    public KVException(String message, Throwable cause) {
        super(message, cause);
    }
}
