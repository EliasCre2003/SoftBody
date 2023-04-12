package eliascregard.physics.rigidbody;

import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public class Box extends Shape {
    
    public final Vector2[] vertices = { new Vector2(), new Vector2(), new Vector2(), new Vector2() };
    
    public final Edge[] edges = { 
        new Edge(vertices[0], vertices[1]), 
        new Edge(vertices[1], vertices[2]), 
        new Edge(vertices[2], vertices[3]), 
        new Edge(vertices[3], vertices[0]) };
    
    public static class Edge {
        
        final Vector2 a;
        final Vector2 b;
        final Vector2 normal = new Vector2();
        
        public Edge(Vector2 a, Vector2 b) {
            this.a = a;
            this.b = b;
        }
        
        void recalculateNormal() {
            normal.set(b);
            normal.subtract(a);
            normal.set(-normal.y(), normal.x());
            normal.normalize();
        }
        
        void draw(Graphics2D g) {
            g.setColor(Color.BLACK);
            g.drawLine((int) a.x(), (int) a.y(), (int) b.x(), (int) b.y());
            // draw normal
            g.setColor(Color.GRAY);
//            double nx1 = a.getX() + (b.getX() - a.getX()) * 0.5;
//            double ny1 = a.y + (b.y - a.y) * 0.5;
//            double nx2 = nx1 + normal.x * 10;
//            double ny2 = ny1 + normal.y * 10;
//            g.drawLine((int) nx1, (int) ny1, (int) nx2, (int) ny2);
        }
    }

    public Box() {
    }

    public Box(double width, double height) {
        this();
        set(width, height);
    }

    private void set(double width, double height) {
        vertices[0].set(-width / 2, height / 2);
        vertices[1].set(width / 2, height / 2);
        vertices[2].set(width / 2, -height / 2);
        vertices[3].set(-width / 2, -height / 2);
        recalculateEdgeNormals();
    }

    public void setShape(Shape shape) {
        if (shape instanceof Box box) {
            for (int v = 0; v < box.vertices.length; v++) {
                vertices[v].set(box.vertices[v]);
            }
            recalculateEdgeNormals();
        }
    }

    private void recalculateEdgeNormals() {
        for (Edge edge : edges) {
            edge.recalculateNormal();
        }
    }
    
    public Vector2[] getVertices() {
        return vertices;
    }

    public Edge[] getEdges() {
        return edges;
    }
    
    private static final Point2D P_SRC = new Point2D.Double();
    private static final Point2D P_DST = new Point2D.Double();
    
    public void convertToWorldSpace(Box box, AffineTransform transform) {
        for (int v = 0; v < box.vertices.length; v++) {
            Vector2 vertex = box.vertices[v];
            P_SRC.setLocation(vertex.x(), vertex.y());
            transform.transform(P_SRC, P_DST);
            vertices[v].set(P_DST.getX(), P_DST.getY());
        }
        recalculateEdgeNormals();
    }
    
    @Override
    public void drawDebug(Graphics2D g) {
        Polygon polygon = new Polygon();
        for (Vector2 vertex : vertices) {
            polygon.addPoint((int) vertex.x(), (int) vertex.y());
        }
        g.setColor(new Color(0xFFFFFF));
        g.fillPolygon(polygon);
    }
    
}
