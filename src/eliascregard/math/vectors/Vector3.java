package eliascregard.math.vectors;

public class Vector3 extends Vector{

    public static final Vector3 ZERO = new Vector3();

    public Vector3(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector3(Vector point1, Vector point2) {
        super(point2, point1);
    }
    public Vector3() {
        this(0, 0, 0);
    }

    public double x() {
        return getParameter(1);
    }

    public double y() {
        return getParameter(2);
    }

    public double z() {
        return getParameter(3);
    }

    public void setX(double x) {
        setParameter(1, x);
    }

    public void setY(double y) {
        setParameter(2, y);
    }

    public void setZ(double z) {
        setParameter(3, z);
    }

    public void set(double x, double y, double z) {
        setX(x); setY(y); setZ(z);
    }

    public void set(Vector3 vector) {
        set(vector.x(), vector.y(), vector.z());
    }

    public Vector3 normalized() {
        return (Vector3) super.normalized();
    }

    public static Vector3 sum(Vector vector1, Vector vector2) {
        return (Vector3) Vector.sum(vector1, vector2);
    }

    public static Vector3 difference(Vector vector1, Vector vector2) {
        return (Vector3) Vector.difference(vector1, vector2);
    }

    public static Vector3 normalizedVector(Vector vector) {
        return (Vector3) Vector.normalizedVector(vector);
    }

    public static Vector3 scalarProduct(Vector vector1, double scalar) {
        return (Vector3) Vector.scalarProduct(vector1, scalar);
    }

    public static Vector3 crossProduct(Vector vector1, Vector vector2) {
        return (Vector3) Vector.crossProduct(vector1, vector2);
    }

    public static Vector3 orthogonalProjection(Vector vector1, Vector vector2) {
        return (Vector3) Vector.orthogonalProjection(vector1, vector2);
    }


}
