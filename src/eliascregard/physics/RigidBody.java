package eliascregard.physics;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;

public class RigidBody {

    private Vector2D position;
    private Vector2D linearVelocity;
    private double angle;
    private double angularVelocity;
    private double torque;
    private Vector2D force;
    private double mass;
    private double momentOfInertia;
    private Vector2D[] vertices;
    private Perimeter perimeter;

    public RigidBody(Vector2D position, double width, double height, double mass, Vector2D[] vertices) {
        this.position = position;
        this.linearVelocity = new Vector2D();
        this.angle = angle;
        this.angularVelocity = 0;
        this.torque = 0;
        this.force = new Vector2D();
        this.mass = mass;
        this.momentOfInertia = mass * (width * width + height * height) / 12;
        this.vertices = vertices;
        calculatePerimeter();
    }

    public void update(double deltaTime, Vector2D gravity) {
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
    public void calculatePerimeter() {
        Vector2D min = vertices[0].makeCopy();
        Vector2D max = vertices[0].makeCopy();
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].x < min.x) {
                min.x = vertices[i].x;
            }
            if (vertices[i].y < min.y) {
                min.y = vertices[i].y;
            }
            if (vertices[i].x > max.x) {
                max.x = vertices[i].x;
            }
            if (vertices[i].y > max.y) {
                max.y = vertices[i].y;
            }
        }
        this.perimeter = new Perimeter(min, max);
    }

    public Vector2D[] getPolygonPoints() {
        return vertices;
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

    public boolean insidePerimeter(RigidBody rigidBody) {
        return Perimeter.perimeterIntersection(this.perimeter, rigidBody.perimeter);
    }


}

