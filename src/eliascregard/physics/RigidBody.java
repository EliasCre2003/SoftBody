package eliascregard.physics;

import java.awt.*;

public class RigidBody {

    private Vector2D position;
    private Vector2D linearVelocity;
    private double angle;
    private double angularVelocity;
    private double torque;
    private Vector2D force;
    private double width;
    private double height;
    private double mass;
    private double momentOfInertia;

    public RigidBody(Vector2D position, double width, double height, double mass, double angle) {
        this.position = position;
        this.linearVelocity = new Vector2D();
        this.angle = angle;
        this.angularVelocity = 0;
        this.torque = 0;
        this.force = new Vector2D();
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.momentOfInertia = mass * (width * width + height * height) / 12;
    }

    public void update(double deltaTime, Vector2D gravity) {
        this.linearVelocity.add(gravity, deltaTime);
        this.linearVelocity.add(this.force, deltaTime / this.mass);
        this.position.add(this.linearVelocity, deltaTime);
        this.angularVelocity += this.torque * deltaTime / this.momentOfInertia;
        this.angle += this.angularVelocity * deltaTime;
        this.force.set(0, 0);
        this.torque = 0;
    }
    public void update(double deltaTime) {
        this.update(deltaTime, new Vector2D(0, 9.82));
    }

    public void applyForce(Vector2D force, Vector2D torqueArm) {
        this.force.add(force);
        torque += force.crossProduct(torqueArm);
    }
    public void applyForce(Vector2D force) {
        applyForce(force, new Vector2D());
    }

    public static Vector2D calculateTorqueArm(Vector2D position, RigidBody rigidBody) {
        return Vector2D.difference(position, rigidBody.position);
    }
    public Vector2D calculateTorqueArm(Vector2D position) {
        return calculateTorqueArm(position, this);
    }

    public Vector2D[] getPolygonPoints() {
        Vector2D[] points = new Vector2D[4];
        points[0] = new Vector2D(-width / 2, -height / 2);
        points[1] = new Vector2D(width / 2, -height / 2);
        points[2] = new Vector2D(width / 2, height / 2);
        points[3] = new Vector2D(-width / 2, height / 2);
        for (Vector2D point : points) {
            point.rotate(angle);
            point.add(position);
        }
        return points;
    }

    public Line[] getPolygonLines() {
        Vector2D[] points = getPolygonPoints();
        Line[] lines = new Line[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = new Line(points[i], points[(i + 1) % 4]);
        }
        return lines;
    }

    public Polygon getPolygon(double scalar) {
        Vector2D[] points = getPolygonPoints();
        Polygon polygon = new Polygon();
        for (Vector2D point : points) {
            polygon.addPoint((int) (point.x * scalar), (int) (point.y * scalar));
        }
        return polygon;
    }

    public boolean resolveCollision(RigidBody otherRigidBody) {
        Line[] lines = getPolygonLines();
        Line[] otherLines = otherRigidBody.getPolygonLines();
        for (Line line : lines) {
            for (Line otherLine : otherLines) {
                if (line.lineLineIntersection(otherLine) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
