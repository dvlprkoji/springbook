package springbook.sqlservice;

public class SqlNotFoundException extends RuntimeException {

    public SqlNotFoundException(String message) {
        super(message);
    }
}
