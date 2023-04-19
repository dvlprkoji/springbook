package springbook.sqlservice;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
