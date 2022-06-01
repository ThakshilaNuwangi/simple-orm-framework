package lk.ijse.dep8.orm.exception;

public class InvalidTableException extends RuntimeException{
    public InvalidTableException() {
        super();
    }

    public InvalidTableException(String message) {
        super(message);
    }
}
