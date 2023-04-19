package springbook.sqlservice;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import springbook.sqlservice.jaxb.SqlType;
import springbook.sqlservice.jaxb.Sqlmap;
import springbook.user.dao.UserDao;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
    private BaseSqlService baseSqlService = new BaseSqlService();


    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmap(Resource sqlmap) {
        this.oxmSqlReader.setSqlmap(sqlmap);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    private void loadSql() {
        baseSqlService.setSqlReader(this.oxmSqlReader);
        baseSqlService.setSqlRegistry(this.sqlRegistry);
        baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key){
        return this.baseSqlService.getSql(key);
    }


    private class OxmSqlReader implements SqlReader{

        private Resource sqlmap = new ClassPathResource("/sqlmap.xml", UserDao.class);
        private Unmarshaller unmarshaller;

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try{
                Source source = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch(IOException e){
                throw new IllegalArgumentException(this.sqlmap.getFilename());
            }
        }

    }
}
