package eliascregard.physics;

import java.awt.*;

public class StaticObject {

    public Vector2D[] vertices;

    public StaticObject(Vector2D[] vertices) {
        this.vertices = vertices;
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

    public void move(Vector2D delta) {
        for (Vector2D vertex : vertices) {
            vertex.add(delta);
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

}
