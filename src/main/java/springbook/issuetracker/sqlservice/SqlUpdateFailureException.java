package springbook.issuetracker.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(String s) {
        super(s);
    }
}
