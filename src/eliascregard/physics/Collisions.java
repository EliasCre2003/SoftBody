package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

public class Collisions {

    public static boolean isColliding(Circle circle1, Circle circle2) {
        double distance = Vector2.distance(circle1.position, circle2.position);
        return distance < (circle1.radius + circle2.radius);
    }

    public static boolean isColliding(Line line, Vector2 point, double buffer) {
        double d1 = Vector2.distance(point, line.getPoint1());
        double d2 = Vector2.distance(point, line.getPoint2());
        double lineLength = line.length();
        return d1 + d2 >= lineLength - buffer && d1 + d2 <= lineLength + buffer;
    }

    public static boolean isColliding(Line line, Vector2 point) {
        return isColliding(line, point, 0.1);
    }

    public static boolean isColliding(Line line1, Line line2) {
        Vector2 p1 = line1.getPoint1();
        Vector2 p2 = line1.getPoint2();
        Vector2 p3 = line2.getPoint1();
        Vector2 p4 = line2.getPoint1();

        double denominator = (p1.getX() - p2.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p2.getY()) * (p3.getX() - p4.getX());
        if (denominator == 0) {
            return false;
        }
        double t = ((p1.getX() - p3.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p3.getY()) * (p3.getX() - p4.getX())) / denominator;
        double u = ((p1.getX() - p2.getX()) * (p1.getY() - p2.getY()) - (p1.getY() - p3.getY()) * (p1.getX() - p2.getX())) / denominator;
        return t >= 0 && t <= 1 && u >= 0 && u <= 1;
    }

    public static boolean isColliding(Line line, Circle circle) {
        Vector2 closestPoint = Line.closestPointOnLineToPoint(line, circle.position);
        return Vector2.distance(circle.position, closestPoint) < circle.radius;
    }

}
