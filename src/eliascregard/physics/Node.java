package eliascregard.physics;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;

public class Node {

    final public static double DEFAULT_NODE_RADIUS = 11;

    public Vector2D position;
    public Vector2D velocity;
    public double mass;
    public double radius;
    public Vector2D totalForceVector;
    public boolean isFixed;

    public Node(Vector2D position, double mass, double radius, boolean isFixed) {
        this.position = position;
        this.velocity = new Vector2D();
        this.mass = mass;
        this.radius = radius;
        this.totalForceVector = new Vector2D();
        this.isFixed = false;
    }
    public Node(Vector2D position, double mass, double radius) {
        this(position, mass, radius, false);
    }
    public Node(Vector2D position) {
        this(position, 1, DEFAULT_NODE_RADIUS);
    }

    public void update(double deltaTime, Vector2D gravity) {
        if (!this.isFixed) {
            Vector2D gravityVector = gravity.makeCopy();
            gravityVector.scale(this.mass);
            this.applyForce(gravityVector);
            this.velocity.add(this.totalForceVector, deltaTime / this.mass);
//            if (this.velocity.length() > 10000) {
//                this.velocity.normalize();
//                this.velocity.scale(10000);
//            }
            this.position.add(this.velocity, deltaTime);
        } else {
            this.velocity.set(0, 0);
        }
        this.totalForceVector.set(0, 0);
    }
    public void update(double deltaTime) {
        this.update(deltaTime, new Vector2D(0, 9.82));
    }

    public void applyForce(Vector2D forceVector) {
        this.totalForceVector.add(forceVector);
    }

    public boolean isColliding(Node otherNode) {
        double distance = Vector2D.distance(this.position, otherNode.position);
        return (distance < this.radius + otherNode.radius) && (distance > 0);
    }

    public void resolveCollision(Node otherNode) {
        double distance = Vector2D.distance(this.position, otherNode.position);
        Vector2D direction = Vector2D.difference(this.position, otherNode.position);
        direction.scale(1 / distance);
        double correction = (this.radius + otherNode.radius - distance) / 2;
        this.position.add(direction, correction);
        otherNode.position.add(direction, -correction);
        if (this.isFixed && otherNode.isFixed) return;
        double totalMass = this.mass + otherNode.mass;
        if (totalMass == 0) return;
        double p = 2 * (this.velocity.dotProduct(direction) - otherNode.velocity.dotProduct(direction)) / totalMass;
        this.velocity.subtract(direction.scaled(p / this.mass));
        otherNode.velocity.add(direction.scaled(p / otherNode.mass));
    }

    public boolean isColliding(Line line) {
        double side1 = Math.sqrt(Math.pow(this.position.x - line.getPoint1().x, 2) + Math.pow(this.position.y - line.getPoint2().y, 2));
        double side2 = Math.sqrt(Math.pow(this.position.x - line.getPoint2().x, 2) + Math.pow(this.position.y - line.getPoint2().y, 2));
        if (side1 < this.radius || side2 < this.radius) {
            return true;
        }

        Vector2D distanceVector = Vector2D.difference(line.getPoint1(), line.getPoint2());
        double dot = (
                (this.position.x - line.getPoint1().x) * (line.getPoint2().x - line.getPoint1().x) +
                (this.position.y - line.getPoint1().y) * (line.getPoint2().y - line.getPoint1().y)
        ) / Math.pow(distanceVector.length(), 2);
        Vector2D closestPoint = new Vector2D(
                line.getPoint1().x + dot * (line.getPoint2().x - line.getPoint1().x),
                line.getPoint1().y + dot * (line.getPoint2().y - line.getPoint1().y)
        );
        if (Line.linePoint(line, closestPoint)) {
            return Vector2D.distance(this.position, closestPoint) < this.radius;
        }
        return false;
    }

    public void resolveCollision(Line line) {
        if (this.isFixed) {
            return;
        }
        double lineAngle = Math.atan2(line.getPoint2().y - line.getPoint1().y, line.getPoint2().x - line.getPoint1().x);
        double velocityAngle = Math.atan2(this.velocity.y, this.velocity.x);
        double reflectionAngle = 2 * lineAngle - velocityAngle;
        Vector2D collisionVector = Vector2D.angleToVector(reflectionAngle);
        collisionVector.scale(this.velocity.length());
        this.velocity = collisionVector;
    }

    public boolean insidePerimeter(StaticObject staticObject) {
        Perimeter perimeter = staticObject.getPerimeter();
        return this.position.x < perimeter.getMax().x && this.position.y < perimeter.getMax().y &&
                this.position.x > perimeter.getMin().x && this.position.y > perimeter.getMin().y;
    }

    public void resolveCollision(StaticObject staticObject) {

        // CHECKS IF THE NODE IS INSIDE THE PERIMETER OF THE STATIC OBJECT
        Line[] polygonLines = staticObject.getLines();
        Line ray = new Line(position, new Vector2D(position.x + Integer.MAX_VALUE, position.y));
        int intersections = 0;
        Vector2D[] closestPoints = new Vector2D[polygonLines.length];
        for (int i = 0; i < polygonLines.length; i++) {
            Vector2D intersectionPoint = Line.lineLineIntersection(ray, polygonLines[i]);
            if (intersectionPoint != null) {
                intersections++;
            }
            closestPoints[i] = Line.closestPointOnLineToPoint(polygonLines[i], position);
        }
        if (intersections % 2 == 0) {
            return;
        }
        // FINDS THE CLOSEST POINT ON THE STATIC OBJECTS PERIMETER TO THE NODE
        int closestIndex = 0;
        double closestDistance = Vector2D.distance(position, closestPoints[0]);
        for (int i = 1; i < closestPoints.length; i++) {
            double distance = Vector2D.distance(position, closestPoints[i]);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }
        Vector2D closestPoint = closestPoints[closestIndex];
        Line closestLine = polygonLines[closestIndex];

        // FINALLY RESOLVES THE COLLISION
        position.set(closestPoint);
        Vector2D lineNormal = closestLine.normal();
        if (lineNormal.length() == 0) return;
        double push = (2 * staticObject.getRestitutionCoefficient() * velocity.dotProduct(lineNormal));
        Vector2D lineNormalForce = lineNormal.scaled(push);
        Vector2D frictionForce = Vector2D.difference(velocity,
                lineNormal.scaled(velocity.dotProduct(lineNormal) / lineNormal.dotProduct(lineNormalForce))
        );
        frictionForce.scale(staticObject.getFrictionCoefficient());
        velocity.subtract(lineNormalForce);
        velocity.subtract(frictionForce);
    }

    public void fix(double x, double y) {
        this.position.set(x, y);
        this.isFixed = true;
    }
    public void fix(Vector2D position) {
        this.fix(position.x, position.y);
    }
    public void fix() {
        this.isFixed = true;
    }
    public void unfix() {
        this.isFixed = false;
    }

    public Circle getCircle() {
        return new Circle(this.position, this.radius);
    }

    public Node makeCopy() {
        return new Node(this.position.makeCopy(), this.mass, this.radius);
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(0, 0, 255));
        g2.fillOval((int)((this.position.x - this.radius)*scale), (int)((this.position.y - this.radius)*scale), (int) (this.radius * 2 * scale), (int) (this.radius * 2 * scale));
    }

    public String toString() {
        return "position: (" + this.position.toString() + "), velocity: (" + this.velocity.toString() + "), mass: " + this.mass + ", radius: " + this.radius;
    }

}
