package learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest {

    public interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    public class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

    public class HelloUpperClass implements Hello {

        private Hello hello;

        public void setHello(Hello hello) {
            this.hello = hello;
        }

        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase(Locale.ROOT);
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase(Locale.ROOT);
        }

        @Override
        public String sayThankYou(String name) {
            return hello.sayThankYou(name).toUpperCase(Locale.ROOT);
        }
    }

    public class UpperCaseHandler implements InvocationHandler {

        Hello target;

        public UpperCaseHandler(Hello target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String ret = (String) method.invoke(target, args);
            return ret.toUpperCase(Locale.ROOT);
        }
    }


    @Test
    public void simpleProxy() {
        Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UpperCaseHandler(new HelloTarget())
        );

    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Koji"), is("HELLO KOJI"));
        assertThat(proxiedHello.sayHi("Koji"), is("HI KOJI"));
        assertThat(proxiedHello.sayThankYou("Koji"), is("THANK YOU KOJI"));
    }

    public static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase(Locale.ROOT);
        }
    }


    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Koji"), is("HELLO KOJI"));
        assertThat(proxiedHello.sayHi("Koji"), is("HI KOJI"));
        assertThat(proxiedHello.sayThankYou("Koji"), is("Thank You Koji"));

    }
}
