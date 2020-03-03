package Sample;

import org.junit.*;

import static org.junit.Assert.assertTrue;

public class SampleTest {


    @Test
    // test method to add two values
    public void testAdd(){
        int value1 = 3;
        int value2 = 3;
        double result = value1 + value2;
        assertTrue(result == 6);
    }

    @Test
    public void testSub(){
        int value1 = 5;
        int value2 = 3;
        int result = value1 - value2;
        assertTrue(result == 2);
    }



}
