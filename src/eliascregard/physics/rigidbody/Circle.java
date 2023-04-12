package eliascregard.physics.rigidbody;

import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Circle extends Shape {

    private double radius;
    private final Vector2 center = new Vector2();

    public Circle(double radius) {
        this.radius = radius;
    }
    public Circle() {
        this(0);
    }
    public Vector2 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    private static final Point2D P_SRC = new Point2D.Double();
    private static final Point2D P_DST = new Point2D.Double();

    public void convertToWorldSpace(Vector2 worldPoint, AffineTransform transform) {
        P_SRC.setLocation(worldPoint.x(), worldPoint.y());
        transform.transform(P_SRC, P_DST);
        worldPoint.set(P_DST.getX(), P_DST.getY());
    }

    public void convertToWorldSpace(Circle circle, AffineTransform transform) {
        convertToWorldSpace(circle.center, transform);
    }

    public void setShape(Shape shape) {
        if (shape instanceof Circle circle) {
            radius = circle.radius;
        }
    }

    public void drawDebug(Graphics2D g2) {
        g2.setColor(new Color(0xFFFFFF));
        g2.fillOval((int) (center.x() - radius), (int) (center.y() - radius), (int) (radius * 2), (int) (radius * 2));
    }


}
