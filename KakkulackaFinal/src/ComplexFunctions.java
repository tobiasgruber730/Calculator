import java.util.HashMap;

/**
 * A class providing complex mathematical functions including trigonometric calculations
 * and factorial computation.
 */
public class ComplexFunctions {
    public static final int DEPTH = 10;
    public static final int[] tableKeys = {0, 30, 45, 60, 90, 120, 135, 150, 180, 210, 225, 240, 270, 300, 315, 330, 360};
    public static final double[] tableValuesSin = {0.0, 0.5, 0.70710678118, 0.86602540378, 1.0, 0.86602540378, 0.70710678118, 0.5, 0.0, -0.5, -0.70710678118, -0.86602540378, -1.0, -0.86602540378, -0.70710678118, -0.5, 0.0};
    public static final double[] tableValuesCos = {1.0, 0.86602540378, 0.70710678118, 0.5, 0.0, -0.5, -0.70710678118, -0.86602540378, -1.0, -0.86602540378, -0.70710678118, -0.5, 0.0, 0.5, 0.70710678118, 0.86602540378, 1.0};

    static HashMap<Integer, Double> sinMap = new HashMap<>();
    static HashMap<Integer, Double> cosMap = new HashMap<>();

    /**
     * Computes the factorial of a given non-negative integer.
     *
     * @param n the integer for which the factorial is to be calculated
     * @return the factorial of the given integer
     * @throws IllegalArgumentException if the input is negative
     */
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial cannot be negative");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     * Initializes the sine and cosine lookup tables.
     */
    public static void setMaps() {
        for (int i = 0; i < tableKeys.length; i++) {
            sinMap.put(tableKeys[i], tableValuesSin[i]);
            cosMap.put(tableKeys[i], tableValuesCos[i]);
        }
    }

    /**
     * Computes the sine of a given angle in degrees using a Taylor series approximation.
     *
     * @param x the angle in degrees
     * @return the sine of the given angle
     */
    public static double sin(double x) {
        if (x - (int) x == 0 && sinMap.containsKey((int) x)) {
            return sinMap.get((int) x);
        }
        double result = 0;
        int sign = 1;
        for (int i = 1; i < DEPTH; i += 2) {
            result += (sign * (Math.pow(x, i) / factorial(i)));
            sign = -sign;
        }
        return result;
    }

    /**
     * Computes the cosine of a given angle in degrees using a Taylor series approximation.
     *
     * @param x the angle in degrees
     * @return the cosine of the given angle
     */
    public static double cos(double x) {
        if (x - (int) x == 0 && cosMap.containsKey((int) x)) {
            return cosMap.get((int) x);
        }
        double result = 0;
        int sign = 1;
        for (int i = 0; i < DEPTH; i += 2) {
            result += (sign * (Math.pow(x, i) / factorial(i)));
            sign = -sign;
        }
        return result;
    }

    /**
     * Computes the tangent of a given angle in degrees.
     *
     * @param x the angle in degrees
     * @return the tangent of the given angle
     */
    public static double tan(double x) {
        return sin(x) / cos(x);
    }

    /**
     * Computes the cotangent of a given angle in degrees.
     *
     * @param x the angle in degrees
     * @return the cotangent of the given angle
     */
    public static double cotg(double x) {
        return cos(x) / sin(x);
    }
}
