package springbook.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import springbook.user.dao.UserDao;

public class UserSqlMapConfig implements SqlMapConfig {

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("/sqlmap.xml", UserDao.class);
    }
}
