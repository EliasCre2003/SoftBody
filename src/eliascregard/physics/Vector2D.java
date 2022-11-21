package eliascregard.physics;

public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D() {
        this(0, 0);
    }
    public void set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D makeCopy() {
        return new Vector2D(this.x, this.y);
    }

    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public Vector2D product(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public void add(Vector2D otherVector, double scalar) {
        this.x += otherVector.x * scalar;
        this.y += otherVector.y * scalar;
    }
    public void add(Vector2D otherVector) {
        this.add(otherVector, 1);
    }
    public Vector2D sum(Vector2D otherVector) {
        Vector2D newVector = this.makeCopy();
        newVector.add(otherVector, 1);
        return newVector;
    }

    public void subtract(Vector2D otherVector, double scalar) {
        this.x -= otherVector.x * scalar;
        this.y -= otherVector.y * scalar;
    }
    public void subtract(Vector2D otherVector) {
        this.subtract(otherVector, 1);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2D normalized() {
        double length = this.length();
        return new Vector2D(this.x / length, this.y / length);
    }
    public void normalize() {
        this.set(this.normalized());
    }

    public double dotProduct(Vector2D otherVector) {
        return this.x * otherVector.x + this.y * otherVector.y;
    }

    public static Vector2D difference(Vector2D vector1, Vector2D vector2) {
        Vector2D newVector = new Vector2D(vector1.x, vector1.y);
        newVector.subtract(vector2);
        return newVector;
    }

    public static double vectorToAngle(Vector2D vector) {
        return Math.atan2(vector.y, vector.x);
    }

    public static Vector2D angleToVector(double angle, double length) {
        Vector2D vector = new Vector2D(Math.cos(angle), Math.sin(angle));
        return vector.product(length);
    }
    public static Vector2D angleToVector(double angle) {
        return angleToVector(angle, 1);
    }

    public static double distance(Vector2D vector1, Vector2D vector2) {
        return difference(vector1, vector2).length();
    }
    public double distance(Vector2D vector2) {
        return distance(this, vector2);
    }

    public void debug(String label) {
        if (!label.equals("")) {
            label += " | ";
        }
        System.out.println(label + "x: " + this.x + ", y: " + this.y);
    }
    public void debug() {
        this.debug("");
    }

    public static void debug(String label, Vector2D[] vectors) {
        if (label != null) {
            System.out.println(label);
        }
        for (Vector2D vector : vectors) {
            vector.debug();
        }
    }
    public static void debug(Vector2D[] vectors) {
        debug(null, vectors);
    }

    public String toString() {
        return "x: " + this.x + ", y: " + this.y;
    }
}
