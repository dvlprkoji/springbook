package springbook.sqlservice;

import org.springframework.oxm.Unmarshaller;
import springbook.sqlservice.jaxb.SqlType;
import springbook.sqlservice.jaxb.Sqlmap;
import springbook.user.dao.UserDao;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    private void loadSql() {
        oxmSqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {

        String sql = sqlRegistry.findSql(key);

        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다");
        }
        else return sql;
    }


    private class OxmSqlReader implements SqlReader{

        private final static String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQLMAP_FILE;
        private Unmarshaller unmarshaller;

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try{
                StreamSource source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch(IOException e){
                throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다", e);
            }
        }

    }
}
