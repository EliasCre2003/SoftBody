package eliascregard.physics;

import java.awt.*;

public class StaticObject {

    public Vector2D[] vertices;
    public double frictionCoefficient;
    public double restitutionCoefficient;

    public StaticObject(Vector2D[] vertices, double frictionCoefficient, double restitutionCoefficient) {
        this.vertices = vertices;
        this.frictionCoefficient = frictionCoefficient;
        this.restitutionCoefficient = restitutionCoefficient;
    }
    public StaticObject(Vector2D[] vertices) {
        this(vertices, 0, 1);
    }

    public Line[] getLines() {
        Line[] lines = new Line[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            lines[i] = new Line(vertices[i], vertices[(i + 1) % vertices.length]);
        }
        return lines;
    }

    public Polygon getPolygon(double scale) {
        Polygon polygon = new Polygon();
        for (Vector2D vertex : vertices) {
            polygon.addPoint((int) (vertex.x * scale), (int) (vertex.y * scale));
        }
        return polygon;
    }
    public Polygon getPolygon() {
        return getPolygon(1);
    }

    public double[] getPerimeter() {
        double[] perimiter = new double[4];
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].x > maxX) {
                maxX = vertices[i].x;
            }
            if (vertices[i].y > maxY) {
                maxY = vertices[i].y;
            }
            if (vertices[i].x < minX) {
                minX = vertices[i].x;
            }
            if (vertices[i].y < minY) {
                minY = vertices[i].y;
            }
        }
        perimiter[0] = maxX;
        perimiter[1] = maxY;
        perimiter[2] = minX;
        perimiter[3] = minY;
        return perimiter;
    }

    public void move(Vector2D delta) {
        for (Vector2D vertex : vertices) {
            vertex.sum(delta);
        }
    }

    public void rotate(double deltaAngle) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2D(
                    vertices[i].x * Math.cos(deltaAngle) - vertices[i].y * Math.sin(deltaAngle),
                    vertices[i].x * Math.sin(deltaAngle) + vertices[i].y * Math.cos(deltaAngle)
            );
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(255,255,255));
        g2.setStroke(new BasicStroke((float) (2*scale)));
        g2.draw(getPolygon(scale));
    }

}
