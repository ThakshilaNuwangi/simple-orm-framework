package lk.ijse.dep8.orm.exception;

public class NoConnectionException extends RuntimeException{
    public NoConnectionException() {
        super();
    }

    public NoConnectionException(String message) {
        super(message);
    }
}
