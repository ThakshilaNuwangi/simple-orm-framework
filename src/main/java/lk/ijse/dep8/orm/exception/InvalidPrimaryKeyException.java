package lk.ijse.dep8.orm.exception;

public class InvalidPrimaryKeyException extends RuntimeException{
    public InvalidPrimaryKeyException() {
        super();
    }

    public InvalidPrimaryKeyException(String message) {
        super(message);
    }
}
