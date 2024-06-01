import java.util.*;

/**
 * A calculator that supports basic arithmetic and scientific operations.
 */
public class Calculator {

    private Map<String, Operation> operations;
    private Map<String, ScientificOperation> scientificOperations;

    /**
     * Constructs a new Calculator and initializes supported operations.
     */
    public Calculator() {
        operations = new HashMap<>();
        scientificOperations = new HashMap<>();
        initializeOperations();
        initializeScientificOperations();
    }

    /**
     * Initializes basic arithmetic operations.
     */
    private void initializeOperations() {
        operations.put("+", new Addition());
        operations.put("-", new Subtraction());
        operations.put("*", new Multiplication());
        operations.put("/", new Division());
        operations.put("^", new Power());
    }

    /**
     * Initializes scientific operations.
     */
    private void initializeScientificOperations() {
        scientificOperations.put("sin", new Sine());
        scientificOperations.put("cos", new Cosine());
        scientificOperations.put("tan", new Tangent());
        scientificOperations.put("log", new Logarithm());
        scientificOperations.put("exp", new Exponential());
        scientificOperations.put("sqrt", new SquareRoot());
        scientificOperations.put("!", new Factorial());
    }

    /**
     * Evaluates a mathematical expression and returns the result.
     *
     * @param expression the mathematical expression to evaluate
     * @return the result of the evaluated expression
     * @throws IllegalArgumentException if the expression is null, empty, or invalid
     */
    public double calculate(String expression) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        System.out.println("Input expression: " + expression);

        if (!isValidExpression(expression)) {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }

        double result = evaluateExpression(expression);
        System.out.println("Result: " + result);
        return result;
    }

    /**
     * Validates the format of a mathematical expression.
     *
     * @param expression the expression to validate
     * @return true if the expression is valid, false otherwise
     */
    private boolean isValidExpression(String expression) {
        String scientificFunctionsRegex = "(sin|cos|tan|log|exp|sqrt)";
        if (expression.matches(".*" + scientificFunctionsRegex + ".*")) {
            String trimmedExpression = expression.replaceAll("\\s+", "");
            if (trimmedExpression.matches(".*" + scientificFunctionsRegex + "\\(.*\\).*") ||
                    trimmedExpression.matches(".*" + scientificFunctionsRegex + "\\d+.*") ||
                    containsFactorial(trimmedExpression)) { // Check for factorial expression
                return true;
            } else {
                System.out.println("Invalid expression format: " + expression);
                return false;
            }
        } else {
            for (int i = 0; i < expression.length(); i++) {
                char character = expression.charAt(i);
                if (!isNumeric(character) && !isOperator(character) && !isParenthesis(character) && !Character.isWhitespace(character) && character != '!') {
                    System.out.println("Invalid character found: " + character);
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Evaluates a valid mathematical expression and returns the result.
     *
     * @param expression the expression to evaluate
     * @return the result of the evaluated expression
     * @throws IllegalArgumentException if the expression format is invalid
     */
    private double evaluateExpression(String expression) {
        Stack<Double> numberStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        String[] tokens = expression.split("(?<=[-+*/()^!])|(?=[-+*/()^!])|(?<=[a-zA-Z])(?=\\d)|(?<=\\d)(?=[a-zA-Z])");

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }

            if (isNumeric(token.charAt(0))) {
                double number = Double.parseDouble(token);
                numberStack.push(number);
            } else if (isOperator(token.charAt(0))) {
                while (!operatorStack.isEmpty() && hasHigherPrecedence(operatorStack.peek(), token)) {
                    processOperator(numberStack, operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (isParenthesis(token.charAt(0))) {
                if (token.charAt(0) == '(') {
                    operatorStack.push(token);
                } else if (token.charAt(0) == ')') {
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                        processOperator(numberStack, operatorStack.pop());
                    }
                    operatorStack.pop();
                }
            } else if (isScientificFunction(token)) {
                operatorStack.push(token);
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperator(numberStack, operatorStack.pop());
        }

        if (numberStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression format");
        }

        return numberStack.pop();
    }

    /**
     * Applies a scientific function to a given operand.
     *
     * @param function the scientific function to apply
     * @param operand  the operand to apply the function to
     * @return the result of the function
     * @throws IllegalArgumentException if the function is invalid
     */
    private double applyScientificFunction(String function, double operand) {
        ScientificOperation scientificOperation = scientificOperations.get(function);
        if (scientificOperation != null) {
            return scientificOperation.apply(operand);
        } else {
            throw new IllegalArgumentException("Invalid scientific function: " + function);
        }
    }

    /**
     * Checks if the first operator has higher precedence over the second operator.
     *
     * @param op1 the first operator
     * @param op2 the second operator
     * @return true if the first operator has higher precedence, false otherwise
     */
    private boolean hasHigherPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) return false;
        if (scientificOperations.containsKey(op1)) return false;
        return (op1.equals("*") || op1.equals("/") || op1.equals("^")) && (op2.equals("+") || op2.equals("-"));
    }

    /**
     * Processes an operator by applying it to the operands from the number stack.
     *
     * @param numberStack the stack of numbers
     * @param operator    the operator to process
     * @throws IllegalArgumentException if the operator is invalid
     */
    private void processOperator(Stack<Double> numberStack, String operator) {
        if (isScientificFunction(operator)) {
            double operand = numberStack.pop();
            double result = applyScientificFunction(operator, operand);
            numberStack.push(result);
        } else {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            Operation operation = operations.get(operator);
            if (operation != null) {
                double result = operation.apply(operand1, operand2);
                numberStack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }
    }

    /**
     * Checks if a character is an arithmetic operator.
     *
     * @param character the character to check
     * @return true if the character is an operator, false otherwise
     */
    private boolean isOperator(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
    }

    /**
     * Checks if a character is a parenthesis.
     *
     * @param character the character to check
     * @return true if the character is a parenthesis, false otherwise
     */
    private boolean isParenthesis(char character) {
        return character == '(' || character == ')';
    }

    /**
     * Checks if a character is numeric.
     *
     * @param character the character to check
     * @return true if the character is numeric, false otherwise
     */
    private boolean isNumeric(char character) {
        return Character.isDigit(character) || character == '.';
    }

    /**
     * Checks if an expression contains a factorial.
     *
     * @param expression the expression to check
     * @return true if the expression contains a factorial, false otherwise
     */
    private boolean containsFactorial(String expression) {
        return expression.contains("!");
    }

    /**
     * Checks if a token is a scientific function.
     *
     * @param token the token to check
     * @return true if the token is a scientific function, false otherwise
     */
    private boolean isScientificFunction(String token) {
        return scientificOperations.containsKey(token.toLowerCase());
    }

    interface Operation {
        double apply(double a, double b);
    }

    interface ScientificOperation {
        double apply(double a);
    }

    class Addition implements Operation {
        @Override
        public double apply(double a, double b) {
            double result = add(a, b);
            return result;
        }

        private double add(double a, double b) {
            double sum = calculateSum(a, b);
            return sum;
        }

        private double calculateSum(double a, double b) {
            return a + b;
        }
    }

    class Cosine implements ScientificOperation {
        @Override
        public double apply(double a) {
            double radians = toRadians(a);
            double result = calculateCosine(radians);
            return result;
        }

        private double toRadians(double degrees) {
            double radians = Math.toRadians(degrees);
            return radians;
        }

        private double calculateCosine(double radians) {
            return ComplexFunctions.cos(radians);
        }
    }

    class Logarithm implements ScientificOperation {
        @Override
        public double apply(double a) {
            double result = calculateLogarithm(a);
            return result;
        }

        private double calculateLogarithm(double a) {
            double logValue = Math.log(a);
            return logValue;
        }
    }

    class Sine implements ScientificOperation {
        @Override
        public double apply(double a) {
            double radians = toRadians(a);
            double result = calculateSine(radians);
            return result;
        }

        private double toRadians(double degrees) {
            double radians = Math.toRadians(degrees);
            return radians;
        }

        private double calculateSine(double radians) {
            return ComplexFunctions.sin(radians);
        }
    }

    class Division implements Operation {
        @Override
        public double apply(double a, double b) {
            validateNonZero(b);
            double result = divide(a, b);
            return result;
        }

        private void validateNonZero(double b) {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
        }

        private double divide(double a, double b) {
            double quotient = calculateQuotient(a, b);
            return quotient;
        }

        private double calculateQuotient(double a, double b) {
            return a / b;
        }
    }

    class Subtraction implements Operation {
        @Override
        public double apply(double a, double b) {
            double result = subtract(a, b);
            return result;
        }

        private double subtract(double a, double b) {
            double difference = calculateDifference(a, b);
            return difference;
        }

        private double calculateDifference(double a, double b) {
            return a - b;
        }
    }

    class SquareRoot implements ScientificOperation {
        @Override
        public double apply(double a) {
            double result = calculateSquareRoot(a);
            return result;
        }

        private double calculateSquareRoot(double a) {
            double sqrtValue = Math.sqrt(a);
            return sqrtValue;
        }
    }

    class Tangent implements ScientificOperation {
        @Override
        public double apply(double a) {
            double radians = toRadians(a);
            double result = calculateTangent(radians);
            return result;
        }

        private double toRadians(double degrees) {
            double radians = Math.toRadians(degrees);
            return radians;
        }

        private double calculateTangent(double radians) {
            return ComplexFunctions.tan(radians);
        }
    }

    class Exponential implements ScientificOperation {
        @Override
        public double apply(double a) {
            double result = calculateExponential(a);
            return result;
        }

        private double calculateExponential(double a) {
            double expValue = Math.exp(a);
            return expValue;
        }
    }

    class Multiplication implements Operation {
        @Override
        public double apply(double a, double b) {
            double result = multiply(a, b);
            return result;
        }

        private double multiply(double a, double b) {
            double product = calculateProduct(a, b);
            return product;
        }

        private double calculateProduct(double a, double b) {
            return a * b;
        }
    }

    class Power implements Operation {
        @Override
        public double apply(double a, double b) {
            double result = calculatePower(a, b);
            return result;
        }

        private double calculatePower(double a, double b) {
            double powerValue = Math.pow(a, b);
            return powerValue;
        }
    }

    class Factorial implements ScientificOperation {
        @Override
        public double apply(double a) {
            validateNonNegative(a);
            validateTooLarge(a);
            double result = calculateFactorial(a);
            return result;
        }

        private void validateNonNegative(double a) {
            if (a < 0) {
                throw new IllegalArgumentException("Factorial is defined only for non-negative integers.");
            }
        }

        private void validateTooLarge(double a) {
            if (a > 10) {
                throw new IllegalArgumentException("Number is too large");
            }
        }

        private double calculateFactorial(double a) {
            return ComplexFunctions.factorial((int) a);
        }
    }
}