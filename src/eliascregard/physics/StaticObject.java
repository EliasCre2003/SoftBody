package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class StaticObject {

    private final Vector2[] vertices;
    private double frictionCoefficient;
    private double restitutionCoefficient;
    private Perimeter perimeter;

    public StaticObject(Vector2[] vertices, double frictionCoefficient, double restitutionCoefficient) {
        this.vertices = vertices;
        this.frictionCoefficient = frictionCoefficient;
        this.restitutionCoefficient = restitutionCoefficient;
        this.calculatePerimeter();
    }
    public StaticObject(Vector2[] vertices) {
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
        for (Vector2 vertex : vertices) {
            polygon.addPoint((int) (vertex.getX() * scale), (int) (vertex.getY() * scale));
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

    public void move(Vector2 delta) {
        for (Vector2 vertex : vertices) {
            vertex.add(delta);
        }
    }

    public void rotate(double deltaAngle) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2(
                    vertices[i].getX() * Math.cos(deltaAngle) - vertices[i].getY() * Math.sin(deltaAngle),
                    vertices[i].getX() * Math.sin(deltaAngle) + vertices[i].getY() * Math.cos(deltaAngle)
            );
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(255,255,255));
        g2.setStroke(new BasicStroke((float) (2*scale)));
        g2.draw(getPolygon(scale));
    }

}
