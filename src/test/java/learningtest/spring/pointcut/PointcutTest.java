package learningtest.spring.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import springbook.learningtest.spring.pointcut.Bean;
import springbook.learningtest.spring.pointcut.Target;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PointcutTest {

    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(int minus(..))");

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                    pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null),
                        is(true));

        assertThat((pointcut.getClassFilter().matches(Target.class) &&
                        pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null)),
                is(false));

        assertThat((pointcut.getClassFilter().matches(Bean.class) &&
                        pointcut.getMethodMatcher().matches(Target.class.getMethod("method", null), null)),
                is(false));
    }

    @Test
    public void pointcut() throws Exception {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
    }

    public void targetClassPointcutMatches(String expression, boolean... expected) throws Exception {

        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }


    public void pointcutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception{

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat((pointcut.getClassFilter().matches(clazz) &&
                        pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null)),
                is(expected));
    }

}
