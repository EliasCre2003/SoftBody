package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

public class Line {
    private final Vector2 point1;
    private final Vector2 point2;
    private double length = -1;
    private Vector2 normal = null;


    public Line(Vector2 point1, Vector2 point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
    public Line(double x1, double y1, double x2, double y2) {
        this.point1 = new Vector2(x1, y1);
        this.point2 = new Vector2(x2, y2);
    }
    public double length() {
        if (length == -1) {
            length = point1.distance(point2);
        }
        return length;
    }
    public Vector2 normal() {
        if (normal == null) {
            normal = new Vector2(point2.getY() - point1.getY(), point1.getX() - point2.getX());
            normal.normalize();
        }
        return normal;
    }

    public Vector2 getPoint1() {
        return this.point1;
    }
    public Vector2 getPoint2() {
        return this.point2;
    }

    public static Vector2 lineLineIntersection(Line line1, Line line2) {
        double x1 = line1.point1.getX(), y1 = line1.point1.getY();
        double x2 = line1.point2.getX(), y2 = line1.point2.getY();
        double x3 = line2.point1.getX(), y3 = line2.point1.getY();
        double x4 = line2.point2.getX(), y4 = line2.point2.getY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }
        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = ((x1 - x2) * (y1 - y2) - (y1 - y3) * (x1 - x2)) / denominator;
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            return new Vector2(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        }
        return null;
    }
    public static Vector2 lineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        return Line.lineLineIntersection(new Line(x1, y1, x2, y2), new Line(x3, y3, x4, y4));
    }
    public static Vector2 lineLineIntersection(Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4) {
        return Line.lineLineIntersection(new Line(point1, point2), new Line(point3, point4));
    }
    public Vector2 lineLineIntersection(Line otherLine) {
        return Line.lineLineIntersection(this, otherLine);
    }

    public static boolean linePoint (Line line, Vector2 point) {
        double d1 = point.distance(line.point1);
        double d2 = point.distance(line.point2);
        double lineLength = line.length();
        double buffer = 0.1;
        return d1 + d2 >= lineLength - buffer && d1 + d2 <= lineLength + buffer;
    }

    public static Vector2 closestPointOnLineToPoint(Line line, Vector2 point) {
        Vector2 P = line.point1;
        Vector2 Q = line.point2;
        Vector2 v = Vector2.difference(Q, P);
        double lambdaS = v.dotProduct(Vector2.difference(point, P)) / v.dotProduct(v);
        if (lambdaS <= 0) {
            return P.clone();
        }
        if (lambdaS >= 1) {
            return Q.clone();
        }
        v.scale(lambdaS);
        return Vector2.sum(P, v);
    }

}
