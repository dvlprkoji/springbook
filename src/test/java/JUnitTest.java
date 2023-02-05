import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class JUnitTest {
    
    static JUnitTest testObject;

    @Test
    public void test1() {
        assertThat(this, is(not(testObject)));
        testObject = this;
    }

    @Test
    public void test2() {
        assertThat(this, is(not(testObject)));
        testObject = this;
    }

    @Test
    public void test3() {
        assertThat(this, is(not(testObject)));
        testObject = this;
    }

}
