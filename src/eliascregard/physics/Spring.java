package eliascregard.physics;

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
        this.restLength = Vector2D.distance(node1.position, node2.position);
    }

    public void update() {

        double deltaLength = Vector2D.distance(node1.position, node2.position) - this.restLength;
        double springForce = deltaLength * this.stiffness;
        Vector2D directionVector = Vector2D.difference(node1.position, node2.position).normalized();
        Vector2D deltaVelocity = Vector2D.difference(node1.velocity, node2.velocity);
        double force = springForce + deltaVelocity.dotProduct(directionVector) * this.dampingFactor;
        Vector2D forceVector = directionVector.makeCopy().scaled(force);
        node2.applyForce(forceVector);
        node1.applyForce(forceVector.negated());

    }

    public double getLength() {
        return Vector2D.distance(node1.position, node2.position);
    }

    public Spring makeCopy() {
        return new Spring(this.node1, this.node2, this.stiffness, this.dampingFactor);
    }

}
