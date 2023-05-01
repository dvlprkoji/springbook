package springbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@Import({SqlServiceContext.class})
public class AppContext {

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

    // application components
    @Autowired
    UserDao userDao;

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        UserLevelUpgradeBasicPolicy userLevelUpgradePolicy = new UserLevelUpgradeBasicPolicy();

        return userLevelUpgradePolicy;
    }


    @Configuration
    @ComponentScan(basePackages = "springbook.user")
    @Profile("test")
    public static class TestAppContext {

        @Autowired
        UserDao userDao;

        @Bean
        public UserService testUserService() {
            return new UserServiceTest.TestUserService();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }

    @Configuration
    @Profile("production")
    public static class ProductionAppContext {

        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            return mailSender;
        }
    }
}
