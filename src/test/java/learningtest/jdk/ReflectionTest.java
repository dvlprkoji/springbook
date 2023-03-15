package learningtest.jdk;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ReflectionTest {

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

    public class HelloUpperClass implements Hello{

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
    public void InvocationHandlerTest() throws Exception {

        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),                        // 다이나믹 프록시가 정의되는 클래스 로더
                new Class[]{Hello.class},                           // 다이나믹 프록시가 구현할 클래스
                new UpperCaseHandler(new HelloTarget())             // InvocationHandler 구현체
        );

        assertThat(proxiedHello.sayHello("Koji"), is("HELLO KOJI"));
        assertThat(proxiedHello.sayHi("Koji"), is("HI KOJI"));
        assertThat(proxiedHello.sayThankYou("Koji"), is("THANK YOU KOJI"));
    }




    @Test
    public void invokeMethod() throws Exception{
        String name = "Spring";

        // length()
        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name), is(6));

        //charAt()
        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(name, 0), is('S'));

    }

    @Test
    public void simpleProxy() {

        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Koji"), is("Hello Koji"));
        assertThat(hello.sayHi("Koji"), is("Hi Koji"));
        assertThat(hello.sayThankYou("Koji"), is("Thank You Koji"));
    }

    @Test
    public void simpleProxyDecorator() {

        HelloUpperClass helloUpperClass = new HelloUpperClass();
        helloUpperClass.setHello(new HelloTarget());

        assertThat(helloUpperClass.sayHello("Koji"), is("HELLO KOJI"));
        assertThat(helloUpperClass.sayHi("Koji"), is("HI KOJI"));
        assertThat(helloUpperClass.sayThankYou("Koji"), is("THANK YOU KOJI"));
    }

}
