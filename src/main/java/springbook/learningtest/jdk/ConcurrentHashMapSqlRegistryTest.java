package springbook.learningtest.jdk;

import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;
import springbook.sqlservice.updatable.ConcurrentHashMapSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
