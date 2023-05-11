package springbook.learningtest.spring.ioc;

import org.junit.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class App {

    @Test
    public void simpleApplicationContextTest() {
        StaticApplicationContext ac = new StaticApplicationContext();

        // RegisterSingleton 사용
        ac.registerSingleton("hello1", Hello.class);
        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1, is(notNullValue()));

        // RootBeanDefinition 사용
        RootBeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        ac.registerBeanDefinition("hello2", helloDef);
        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));

        assertThat(hello1, is(not(hello2)));
        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }
}
