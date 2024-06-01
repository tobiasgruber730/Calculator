import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Calculator class.
 */
public class CalculatorTest {

    private Calculator calculator = new Calculator();

    /**
     * Tests the cosine function with an angle of 60 degrees.
     */
    @Test
    public void testCosine() {
        double result = calculator.calculate("cos(60)");
        assertEquals(0.5, result, 0.01, "cos(60) should be 0.5");
    }

    /**
     * Tests the tangent function with an angle of 45 degrees.
     */
    @Test
    public void testTangent() {
        double result = calculator.calculate("tan(45)");
        assertEquals(1, result, 0.01, "tan(45) should be 1");
    }

    /**
     * Tests the logarithm function with a value of 1.
     */
    @Test
    public void testLogarithm() {
        double result = calculator.calculate("log(1)");
        assertEquals(0, result, 0.01, "log(1) should be 0");
    }

    /**
     * Tests the exponential function with a value of 1.
     */
    @Test
    public void testExponential() {
        double result = calculator.calculate("exp(1)");
        assertEquals(Math.E, result, 0.01, "exp(1) should be e");
    }

    /**
     * Tests the square root function with a value of 4.
     */
    @Test
    public void testSquareRoot() {
        double result = calculator.calculate("sqrt(4)");
        assertEquals(2, result, "sqrt(4) should be 2");
    }
}
