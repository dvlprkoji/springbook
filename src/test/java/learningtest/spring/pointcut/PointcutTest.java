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
}
