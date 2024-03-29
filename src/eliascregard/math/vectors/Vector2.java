package eliascregard.math.vectors;

public class Vector2 extends Vector {

    public static final Vector2 ZERO = new Vector2();

    public Vector2(double x, double y) {
        super(x, y);
    }

    public Vector2(Vector point1, Vector point2) {
        super(point2, point1);
    }
    public Vector2() {
        this(0, 0);
    }
    public Vector2(Vector vector) {
        this(vector.getParameter(1), vector.getParameter(2));
    }

    public double getX() {
        return getParameter(1);
    }

    public double getY() {
        return getParameter(2);
    }

    public void setX(double x) {
        setParameter(1, x);
    }

    public void setY(double y) {
        setParameter(2, y);
    }

    public void set(double x, double y) {
        setX(x); setY(y);
    }

    public Vector2 scaled(double scalar) {
        return new Vector2(Vector.scalarProduct(this, scalar));
    }

    public void set(Vector2 vector) {
        set(vector.getX(), vector.getY());
    }

    public Vector2 normalized() {
        return new Vector2(super.normalized());
    }

    public static Vector2 sum(Vector2 vector1, Vector2 vector2) {
        return new Vector2(Vector.sum(vector1, vector2));
    }

    public static Vector2 difference(Vector vector1, Vector vector2) {
        return new Vector2(Vector.difference(vector1, vector2));
    }

    public static Vector2 angleToVector(double angle) {
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }
    public static Vector2 normalizedVector(Vector vector) {
        return new Vector2(Vector.normalizedVector(vector));
    }

    public static Vector2 scalarProduct(Vector vector1, double scalar) {
        return new Vector2(Vector.scalarProduct(vector1, scalar));
    }

    public static Vector2 orthogonalProjection(Vector vector1, Vector vector2) {
        return new Vector2(Vector.orthogonalProjection(vector1, vector2));
    }

    public Vector2 clone() {
        return new Vector2(super.clone());
    }

    public static void main(String[] args) {
        Vector2 vector1 = new Vector2(1, 2);
        Vector2 vector2 = vector1.clone();
        vector2.setX(3);
        System.out.println(vector1.getX());
    }

}
