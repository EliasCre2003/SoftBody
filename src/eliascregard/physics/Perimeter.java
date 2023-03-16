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
        double maxX = vertices[0].getX();
        double maxY = vertices[0].getY();
        double minX = vertices[0].getX();
        double minY = vertices[0].getY();
        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].getX() > maxX) {
                maxX = vertices[i].getX();
            }
            if (vertices[i].getY() > maxY) {
                maxY = vertices[i].getY();
            }
            if (vertices[i].getX() < minX) {
                minX = vertices[i].getX();
            }
            if (vertices[i].getY() < minY) {
                minY = vertices[i].getY();
            }
        }
        this.min = new Vector2(minX, minY);
        this.max = new Vector2(maxX, maxY);
    }

    public static boolean perimeterIntersection(Perimeter perimeter1, Perimeter perimeter2) {
        return perimeter1.min.getX() <= perimeter2.max.getX() && perimeter1.max.getX() >= perimeter2.min.getX() &&
               perimeter1.min.getY() <= perimeter2.max.getY() && perimeter1.max.getY() >= perimeter2.min.getY();
    }

    public Vector2 getMin() {
        return this.min;
    }
    public Vector2 getMax() {
        return this.max;
    }

}
