package springbook.learningtest.spring.embeddeddb;

import org.junit.After;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;
import springbook.issuetracker.sqlservice.updatable.EmbeddedDbSqlRegistry;
import springbook.learningtest.jdk.AbstractUpdatableSqlRegistryTest;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {

        db = new EmbeddedDatabaseBuilder().
                setType(HSQL).
                addScript("classpath:sqlRegistrySchema.sql").
                build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }
}
