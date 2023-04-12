package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

public class Perimeter {

    private final Vector2 min;
    private final Vector2 max;

    public Perimeter(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }

    public Perimeter(double minX, double minY, double maxX, double maxY) {
        this(new Vector2(minX, minY), new Vector2(maxX, maxY));
    }

    public Perimeter(Vector2[] vertices) {
        double maxX = vertices[0].x();
        double maxY = vertices[0].y();
        double minX = vertices[0].x();
        double minY = vertices[0].y();
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].x() > maxX) {
                maxX = vertices[i].x();
            }
            if (vertices[i].y() > maxY) {
                maxY = vertices[i].y();
            }
            if (vertices[i].x() < minX) {
                minX = vertices[i].x();
            }
            if (vertices[i].y() < minY) {
                minY = vertices[i].y();
            }
        }
        this.min = new Vector2(minX, minY);
        this.max = new Vector2(maxX, maxY);
    }

    public static boolean perimeterIntersection(Perimeter perimeter1, Perimeter perimeter2) {
        return perimeter1.min.x() <= perimeter2.max.x() && perimeter1.max.x() >= perimeter2.min.x() &&
               perimeter1.min.y() <= perimeter2.max.y() && perimeter1.max.y() >= perimeter2.min.y();
    }

    public Vector2 getMin() {
        return this.min;
    }
    public Vector2 getMax() {
        return this.max;
    }

}
