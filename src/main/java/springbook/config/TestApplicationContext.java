package springbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("/applicationContext.xml")
public class TestApplicationContext {

}
