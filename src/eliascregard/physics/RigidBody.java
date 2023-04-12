package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class RigidBody {

    private Vector2 position;
    private Vector2 linearVelocity;
    private double angle;
    private double angularVelocity;
    private double torque;
    private Vector2 force;
    private double mass;
    private double momentOfInertia;
    private Vector2[] vertices;
    private Perimeter perimeter;

    public RigidBody(Vector2 position, double width, double height, double mass, Vector2[] vertices) {
        this.position = position;
        this.linearVelocity = new Vector2();
        this.angle = angle;
        this.angularVelocity = 0;
        this.torque = 0;
        this.force = new Vector2();
        this.mass = mass;
        this.momentOfInertia = mass * (width * width + height * height) / 12;
        this.vertices = vertices;
        calculatePerimeter();
    }

    public void update(double deltaTime, Vector2 gravity) {
        this.linearVelocity.add(gravity, deltaTime);
        this.linearVelocity.add(this.force, deltaTime / this.mass);
        this.position.add(this.linearVelocity, deltaTime);
        this.angularVelocity += this.torque * deltaTime / this.momentOfInertia;
        this.angle += this.angularVelocity * deltaTime;
        this.force.set(0, 0);
        this.torque = 0;
        calculatePerimeter();
    }
    public void update(double deltaTime) {
        this.update(deltaTime, new Vector2(0, 9.82));
    }

    public void applyForce(Vector2 force, Vector2 torqueArm) {
        this.force.add(force);
//        torque += force.crossProduct(torqueArm);
    }
    public void applyForce(Vector2 force) {
        applyForce(force, new Vector2());
    }

    public static Vector2 calculateTorqueArm(Vector2 position, RigidBody rigidBody) {
        return Vector2.difference(position, rigidBody.position);
    }
    public Vector2 calculateTorqueArm(Vector2 position) {
        return calculateTorqueArm(position, this);
    }
    public void calculatePerimeter() {
        Vector2 min = vertices[0].clone();
        Vector2 max = vertices[0].clone();
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].x() < min.x()) {
                min.setX(vertices[i].x());
            }
            if (vertices[i].y() < min.y()) {
                min.setY(vertices[i].y());
            }
            if (vertices[i].x() > max.x()) {
                max.setX(vertices[i].x());
            }
            if (vertices[i].y() > max.y()) {
                max.setY(vertices[i].y());
            }
        }
        this.perimeter = new Perimeter(min, max);
    }

    public Vector2[] getPolygonPoints() {
        return vertices;
    }

    public Line[] getPolygonLines() {
        Vector2[] points = getPolygonPoints();
        Line[] lines = new Line[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = new Line(points[i], points[(i + 1) % 4]);
        }
        return lines;
    }

    public Polygon getPolygon(double scalar) {
        Vector2[] points = getPolygonPoints();
        Polygon polygon = new Polygon();
        for (Vector2 point : points) {
            polygon.addPoint((int) (point.x() * scalar), (int) (point.y() * scalar));
        }
        return polygon;
    }

    public boolean insidePerimeter(RigidBody rigidBody) {
        return Perimeter.perimeterIntersection(this.perimeter, rigidBody.perimeter);
    }


}

