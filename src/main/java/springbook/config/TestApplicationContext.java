package springbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.issuetracker.sqlservice.updatable.EmbeddedDbSqlRegistry;
import springbook.sqlservice.OxmSqlService;
import springbook.sqlservice.SqlRegistry;
import springbook.sqlservice.SqlService;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserLevelUpgradeBasicPolicy;
import springbook.user.dao.UserLevelUpgradePolicy;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceTest;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")
public class TestApplicationContext {


    //  db
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springbook");
        dataSource.setUsername("springbook");
        dataSource.setPassword("Springbook!");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    //  sql service
    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());

        return sqlService;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("sqlRegistrySchema.sql")
                .build();
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("springbook.sqlservice.jaxb");

        return unmarshaller;
    }

    // application components

    @Autowired UserDao userDao;

    @Bean
    public UserService testUserService() {
        UserServiceTest.TestUserService testUserService = new UserServiceTest.TestUserService();
        testUserService.setUserDao(this.userDao);
        testUserService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
        testUserService.setMailSender(mailSender());

        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        DummyMailSender mailSender = new DummyMailSender();

        return mailSender;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        UserLevelUpgradeBasicPolicy userLevelUpgradePolicy = new UserLevelUpgradeBasicPolicy();

        return userLevelUpgradePolicy;
    }
}
