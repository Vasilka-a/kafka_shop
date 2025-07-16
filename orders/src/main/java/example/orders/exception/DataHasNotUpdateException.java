package example.orders.exception;

public class DataHasNotUpdateException extends RuntimeException {
    public DataHasNotUpdateException(String message) {
        super(message);
    }
}
