package eliascregard.math.vectors;

public class Vector3D {

    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3D() {
        this(0, 0, 0);
    }
    public void set(Vector3D vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3D makeCopy() {
        return new Vector3D(this.x, this.y, this.z);
    }
    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }
    public Vector3D scaled(double scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }
    public void negate() {
        this.scale(-1);
    }
    public Vector3D negated() {
        return this.scaled(-1);
    }
    public void add(Vector3D otherVector, double scalar) {
        this.x += otherVector.x * scalar;
        this.y += otherVector.y * scalar;
        this.z += otherVector.z * scalar;
    }
    public void add(Vector3D otherVector) {
        this.add(otherVector, 1);
    }
    public Vector3D sum(Vector3D otherVector) {
        Vector3D newVector = this.makeCopy();
        newVector.add(otherVector, 1);
        return newVector;
    }
    public void subtract(Vector3D otherVector, double scalar) {
        this.add(otherVector, -scalar);
    }
    public void subtract(Vector3D otherVector) {
        this.subtract(otherVector, 1);
    }
    public Vector3D difference(Vector3D otherVector) {
        Vector3D newVector = this.makeCopy();
        newVector.subtract(otherVector);
        return newVector;
    }
    public double dotProduct(Vector3D otherVector) {
        return this.x * otherVector.x + this.y * otherVector.y + this.z * otherVector.z;
    }
    public Vector3D crossProduct(Vector3D otherVector) {
        return new Vector3D(
            this.y * otherVector.z - this.z * otherVector.y,
            this.z * otherVector.x - this.x * otherVector.z,
            this.x * otherVector.y - this.y * otherVector.x
        );
    }
    public double magnitude() {
        return Math.sqrt(this.dotProduct(this));
    }
    public void normalize() {
        this.scale(1 / this.magnitude());
    }
    public Vector3D normalized() {
        return this.scaled(1 / this.magnitude());
    }
    public void rotate(Vector3D axis, double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x = this.x;
        double y = this.y;
        double z = this.z;
        this.x = (cos + (1 - cos) * axis.x * axis.x) * x + ((1 - cos) * axis.x * axis.y - axis.z * sin) * y + ((1 - cos) * axis.x * axis.z + axis.y * sin) * z;
        this.y = ((1 - cos) * axis.x * axis.y + axis.z * sin) * x + (cos + (1 - cos) * axis.y * axis.y) * y + ((1 - cos) * axis.y * axis.z - axis.x * sin) * z;
        this.z = ((1 - cos) * axis.x * axis.z - axis.y * sin) * x + ((1 - cos) * axis.y * axis.z + axis.x * sin) * y + (cos + (1 - cos) * axis.z * axis.z) * z;
    }
    public Vector3D rotated(Vector3D axis, double angle) {
        Vector3D newVector = this.makeCopy();
        newVector.rotate(axis, angle);
        return newVector;
    }
}
