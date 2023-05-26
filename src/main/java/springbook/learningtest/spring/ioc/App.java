package springbook.learningtest.spring.ioc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import springbook.learningtest.spring.ioc.bean.AnnotatedHello;
import springbook.learningtest.spring.ioc.bean.AnnotatedHelloConfig;

import java.util.HashSet;

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

    @Test
    public void genericApplicationContext() {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("/genericApplicationContext.xml");

        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericXmlApplicationContext() {
        GenericApplicationContext ac = new GenericXmlApplicationContext("/genericApplicationContext.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void parentContextTest() {
        ApplicationContext parent = new GenericXmlApplicationContext("parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("childContext.xml");

        child.refresh();

        Hello hello = child.getBean("hello", Hello.class);
        assertThat(hello.sayHello(), is("Hello Child"));

    }

    @Test
    public void simpleBeanScanning() {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc.bean");

        AnnotationConfigApplicationContext ctx2 =
                new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);

        AnnotatedHello annotatedHello = ctx.getBean("annotatedHello", AnnotatedHello.class);
        AnnotatedHello annotatedHello2 = ctx2.getBean("annotatedHello", AnnotatedHello.class);

        assertThat(annotatedHello, is(notNullValue()));
        assertThat(annotatedHello2, is(notNullValue()));
    }

    @Test
    public void singletonScope() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
        HashSet<SingletonBean> beans = new HashSet<>();

        beans.add(ac.getBean(SingletonBean.class));
        beans.add(ac.getBean(SingletonBean.class));

        assertThat(beans.size(), is(1));
    }

    static class SingletonBean{}

    static class SingletonClientBean{

        @Autowired
        SingletonBean bean1;
        @Autowired
        SingletonBean bean2;
    }

    @Test
    public void prototypeScope() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
        HashSet<PrototypeBean> bean = new HashSet<>();

        bean.add(ac.getBean(PrototypeBean.class));
        assertThat(bean.size(), is(1));

        bean.add(ac.getBean(PrototypeBean.class));
        assertThat(bean.size(), is(2));

        bean.add(ac.getBean(PrototypeClientBean.class).bean1);
        assertThat(bean.size(), is(3));

        bean.add(ac.getBean(PrototypeClientBean.class).bean2);
        assertThat(bean.size(), is(4));
    }

    @Scope("prototype")
    static class PrototypeBean{}

    static class PrototypeClientBean{

        @Autowired
        PrototypeBean bean1;
        @Autowired
        PrototypeBean bean2;
    }
}
