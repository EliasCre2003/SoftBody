package eliascregard.math.calculator;

public interface Calculator<T extends Number> {
    public T add(T a, T b);
    public T subtract(T a, T b);
    public T multiply(T a, T b);
    public T divide(T a, T b);
    public T power(T a, T b);
    public T root(T a, T b);
}
