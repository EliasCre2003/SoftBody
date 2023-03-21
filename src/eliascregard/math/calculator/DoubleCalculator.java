package eliascregard.math.calculator;

public class DoubleCalculator implements Calculator<Double> {
    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double power(Double a, Double b) {
        return Math.pow(a, b);
    }

    @Override
    public Double root(Double a, Double b) {
        return Math.pow(a, 1.0 / b);
    }

}
