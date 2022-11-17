package eliascregard.physics;

import eliascregard.Line;

public class Node {

    final public static double NODE_RADIUS = 11;

    public Vector2D position;
    public Vector2D velocity;
    public double mass;

    public Node(Vector2D position, double mass) {
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.mass = mass;
    }
    public Node(Vector2D position) {
        this(position, 1);
    }

    public void update(double deltaTime, Vector2D gravity) {
        Vector2D gravityVector = gravity.makeCopy();
        gravityVector.scale(this.mass);
        this.applyForce(gravityVector, deltaTime);
        this.position.add(this.velocity, deltaTime);
    }
    public void update(double deltaTime) {
        this.update(deltaTime, new Vector2D(0, 9.82));
    }

    public void applyForce(Vector2D forceVector, double deltaTime) {
        this.velocity.add(forceVector, deltaTime / this.mass);
    }
    public void applyForce(Vector2D forceVector) {
        this.applyForce(forceVector, 1);
    }

    public boolean isColliding(Node otherNode) {
        double distance = Vector2D.distance(this.position, otherNode.position);
        return (distance < 2 * NODE_RADIUS) && (distance > 0);
    }

    public void resolveCollision(Node otherNode) {

        double distance = Vector2D.distance(this.position, otherNode.position);
        Vector2D direction = Vector2D.subtractVectors(this.position, otherNode.position);
        direction.scale(1 / distance);
        double correction = (2 * NODE_RADIUS - distance) / 2;
        this.position.add(direction, correction);
        otherNode.position.add(direction, -correction);
        double p = 2 * (this.velocity.dotProduct(direction) - otherNode.velocity.dotProduct(direction)) / (this.mass + otherNode.mass);
        if (Double.isNaN(p)) {
            System.out.println("p is NaN");
            return;
        }
        Vector2D pushVector1 = direction.makeCopy();
        pushVector1.scale(p * this.mass);
        this.velocity.subtract(pushVector1);
        Vector2D pushVector2 = direction.makeCopy();
        pushVector2.scale(p * otherNode.mass);
        otherNode.velocity.add(pushVector2,1);

    }

    public boolean isColliding(Line line) {
        double side1 = Math.sqrt(Math.pow(this.position.x - line.point1.x, 2) + Math.pow(this.position.y - line.point2.y, 2));
        double side2 = Math.sqrt(Math.pow(this.position.x - line.point2.x, 2) + Math.pow(this.position.y - line.point2.y, 2));
        if (side1 < NODE_RADIUS || side2 < NODE_RADIUS) {
            return true;
        }

        Vector2D distanceVector = Vector2D.subtractVectors(line.point1, line.point2);
        double dot = (
                (this.position.x - line.point1.x) * (line.point2.x - line.point1.x) +
                (this.position.y - line.point1.y) * (line.point2.y - line.point1.y)
        ) / Math.pow(distanceVector.length(), 2);
        Vector2D closestPoint = new Vector2D(
                line.point1.x + dot * (line.point2.x - line.point1.x),
                line.point1.y + dot * (line.point2.y - line.point1.y)
        );
        if (Line.linePoint(line, closestPoint)) {
            return Vector2D.distance(this.position, closestPoint) < NODE_RADIUS;
        }
        return false;
    }

    public void resolveCollision(Line line, double deltaTime) {
        double lineAngle = Math.atan2(line.point2.y - line.point1.y, line.point2.x - line.point1.x);
        double velocityAngle = Math.atan2(this.velocity.y, this.velocity.x);
        double reflectionAngle = 2 * lineAngle - velocityAngle;
        Vector2D collisionVector = Vector2D.angleToVector(reflectionAngle);
        collisionVector.scale(this.velocity.length());
        this.velocity = collisionVector;
    }



}
