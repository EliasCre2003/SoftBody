package eliascregard.math.calculator;

public class IntCalculator implements Calculator<Integer> {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer power(Integer a, Integer b) {
        return (int) Math.pow(a, b);
    }

    @Override
    public Integer root(Integer a, Integer b) {
        return (int) Math.pow(a, 1.0 / b);
    }

}