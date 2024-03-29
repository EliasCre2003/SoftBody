package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class Spring {

    public Node node1;
    public Node node2;
    public double stiffness;
    public double dampingFactor;
    public double restLength;
    public double breakingForce;

    public Spring(Node node1, Node node2, double stiffness, double dampingFactor) {
        this.node1 = node1;
        this.node2 = node2;
        this.stiffness = stiffness;
        this.dampingFactor = dampingFactor;
        if (this.dampingFactor < 0) {
            this.dampingFactor = 0;
        }
        this.restLength = Vector2.distance(node1.position, node2.position);
    }

    public void update() {

        double deltaLength = Vector2.distance(node1.position, node2.position) - this.restLength;
        double springForce = deltaLength * this.stiffness;
        Vector2 directionVector = Vector2.difference(node1.position, node2.position).normalized();
        Vector2 deltaVelocity = Vector2.difference(node1.velocity, node2.velocity);
        double force = springForce + deltaVelocity.dotProduct(directionVector) * this.dampingFactor;
        Vector2 forceVector = directionVector.scaled(force);
        node2.applyForce(forceVector);
        node1.applyForce(forceVector.scaled(-1));

    }

    public double getLength() {
        return Vector2.distance(node1.position, node2.position);
    }

    public Spring makeCopy() {
        return new Spring(this.node1, this.node2, this.stiffness, this.dampingFactor);
    }


    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(255,255,255));
        g2.setStroke(new BasicStroke((float) (3*scale)));
        g2.drawLine((int)(this.node1.position.getX()*scale), (int)(this.node1.position.getY()*scale),
                (int)(this.node2.position.getX()*scale), (int)(this.node2.position.getY()*scale));
    }
}
