package eliascregard.physics;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;

public class StaticObject {

    private final Vector2D[] vertices;
    private double frictionCoefficient;
    private double restitutionCoefficient;
    private Perimeter perimeter;

    public StaticObject(Vector2D[] vertices, double frictionCoefficient, double restitutionCoefficient) {
        this.vertices = vertices;
        this.frictionCoefficient = frictionCoefficient;
        this.restitutionCoefficient = restitutionCoefficient;
        this.calculatePerimeter();
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
    public double getRestitutionCoefficient() {
        return this.restitutionCoefficient;
    }
    public double getFrictionCoefficient() {
        return this.frictionCoefficient;
    }
    public void setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public void calculatePerimeter() {
        perimeter = new Perimeter(vertices);
    }

    public Perimeter getPerimeter() {
        return perimeter;
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
