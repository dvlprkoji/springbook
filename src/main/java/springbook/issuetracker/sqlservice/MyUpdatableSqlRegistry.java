package springbook.issuetracker.sqlservice;

import springbook.sqlservice.SqlNotFoundException;

import java.util.Map;

public class MyUpdatableSqlRegistry implements UpdatableSqlRegistry{
    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {

    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {

    }

    @Override
    public void registerSql(String key, String sql) {

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }
}
