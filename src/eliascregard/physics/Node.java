package eliascregard.physics;

public class Node {

    final public static double DEFAULT_NODE_RADIUS = 11;

    public Vector2D position;
    public Vector2D previousPosition;
    public Vector2D velocity;
    public double mass;
    public double radius;
    public Vector2D totalForceVector;
    public Vector2D lastForceVector;

    public Node(Vector2D position, double mass, double radius) {
        this.position = position;
        this.previousPosition = position.makeCopy();
        this.velocity = new Vector2D(0, 0);
        this.mass = mass;
        this.radius = radius;
        this.totalForceVector = new Vector2D(0, 0);
        this.lastForceVector = new Vector2D(0, 0);
    }
    public Node(Vector2D position) {
        this(position, 1, DEFAULT_NODE_RADIUS);
    }

    public void update(double deltaTime, Vector2D gravity) {
        Vector2D gravityVector = gravity.makeCopy();
        gravityVector.scale(this.mass);
        this.applyForce(gravityVector);
        this.lastForceVector = this.totalForceVector.makeCopy();
        this.velocity.add(this.totalForceVector, deltaTime / this.mass);
        this.position.add(this.velocity, deltaTime);
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
        if (side1 < this.radius || side2 < this.radius) {
            return true;
        }

        Vector2D distanceVector = Vector2D.difference(line.point1, line.point2);
        double dot = (
                (this.position.x - line.point1.x) * (line.point2.x - line.point1.x) +
                (this.position.y - line.point1.y) * (line.point2.y - line.point1.y)
        ) / Math.pow(distanceVector.length(), 2);
        Vector2D closestPoint = new Vector2D(
                line.point1.x + dot * (line.point2.x - line.point1.x),
                line.point1.y + dot * (line.point2.y - line.point1.y)
        );
        if (Line.linePoint(line, closestPoint)) {
            return Vector2D.distance(this.position, closestPoint) < this.radius;
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

    public boolean insidePerimeter(StaticObject staticObject) {
        double[] perimeter = staticObject.getPerimeter();
        return this.position.x < perimeter[0] && this.position.y < perimeter[1] &&
                this.position.x > perimeter[2] && this.position.y > perimeter[3];
    }

    public void resolveCollision(StaticObject staticObject) {
        Line[] polygonLines = staticObject.getLines();
        Line ray = new Line(this.position, new Vector2D(this.position.x + 100000000, this.position.y));
        int intersections = 0;
        Vector2D[] closestPoints = new Vector2D[polygonLines.length];
        for (int i = 0; i < polygonLines.length; i++) {
            Vector2D intersectionPoint = Line.lineLineIntersection(ray, polygonLines[i]);
            if (intersectionPoint != null) {
                intersections++;
            }
            closestPoints[i] = Line.closestPointOnLineToPoint(polygonLines[i], this.position);
        }
        if (intersections % 2 == 0) {
            return;
        }
        Vector2D closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        for (Vector2D intersectionPoint : closestPoints) {
            if (intersectionPoint == null) {
                continue;
            }
            double distance = Vector2D.distance(this.position, intersectionPoint);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = intersectionPoint;
            }
        }
        Vector2D direction = Vector2D.difference(this.position, closestPoint);
        if (direction.length() == 0) {
            return;
        }
        direction.scale(1 / direction.length());
        double correction = (this.radius - Vector2D.distance(this.position, closestPoint)) / 10;
        this.position.add(direction, -correction);
        double p = (staticObject.restitutionCoefficient + 1) * this.velocity.dotProduct(direction);
        Vector2D pushVector = direction.makeCopy();
        pushVector.scale(p);
        this.velocity.subtract(pushVector);


    }

    public Circle getCircle() {
        return new Circle(this.position, this.radius);
    }

    public Node makeCopy() {
        return new Node(this.position.makeCopy(), this.mass, this.radius);
    }



}
