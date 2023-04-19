package springbook.sqlservice;

import springbook.user.dao.UserDao;
import springbook.sqlservice.jaxb.SqlType;
import springbook.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private String sqlMapFile;

    private Map<String, String> sqlMap;


    private SqlRegistry sqlRegistry;
    private SqlReader sqlReader;

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public XmlSqlService() {
        sqlMap = new HashMap<String, String>();
    }

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }

    @PostConstruct
    public void loadSql() throws JAXBException {
        this.sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if(sql == null) throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다");
        else return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try{
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(this.sqlMapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch(JAXBException e){
            throw new RuntimeException(e);
        }
    }
}
