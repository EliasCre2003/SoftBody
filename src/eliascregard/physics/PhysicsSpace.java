package eliascregard.physics;

import eliascregard.input.KeyHandler;
import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.util.Arrays;

public class PhysicsSpace {

    private final static SpringBody DEFAULT_SPRING_BODY = SpringBody.homogeneousRectangle(
            0,  0, 20, 2, 1, 2000, 100, 5, 10
    );

    private final static SpringBody CIRCLE_SPRING_BODY = SpringBody.simpleCircle(
            0,  0, 100, 50, 3, 1500, 50, 5
    );

    private final static SpringBody TRIANGLE_SPRING_BODY = SpringBody.homogeneousTriangle(
            0,  0, 15, 1, 3000, 100, 5, 10
    );

    private final static SpringBody HEXAGON_SPRING_BODY = SpringBody.homogeneousHexagon(
            0,  0, 4, 1, 3000, 100, 5, 10
    );

    private Vector2 gravity = new Vector2();
    private SpringBody[] springBodies = new SpringBody[0];
    private StaticObject[] staticObjects = new StaticObject[0];
    private Boundary boundary = null;
    private final Vector2 movement = new Vector2();
    private int totalNodes = 0;
    private int totalSprings = 0;
    private SpringBody selectedBody = DEFAULT_SPRING_BODY;
    private final double maximumDeltaTime = 1.0 / 1000;


    public PhysicsSpace() {
    }

    public PhysicsSpace(Vector2 gravity) {
        this.gravity = gravity;
        addSpringBody(SpringBody.load("Default.txt"));

    }

    private void update(double deltaTime) {
        if (deltaTime > maximumDeltaTime) {
            deltaTime = maximumDeltaTime;
        }
        for (SpringBody body : springBodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                if (boundary != null) {
                    if (node.position.getX() > boundary.position.getX() + boundary.width || node.position.getX() < boundary.position.getX()
                        || node.position.getY() > boundary.position.getY() + boundary.height || node.position.getY() < boundary.position.getY()) {
                        node.resolveCollision(boundary);
                    }
                }

                for (StaticObject staticObject : staticObjects) {
                    if (node.insidePerimeter(staticObject)) {
                        node.resolveCollision(staticObject);
                    }
                }

                for (SpringBody otherBody : springBodies) {
                    for (Node otherNode : otherBody.nodes) {
                        if (node.isColliding(otherNode) && node != otherNode) {
                            node.resolveCollision(otherNode);
                        }
                    }
                }
            }
        }

        movement.set(0, 0);

        for (SpringBody body : springBodies) {
            body.update(deltaTime, gravity);
        }
    }

    public void update(double deltaTime, KeyHandler keys, MouseHandler mouse) {
        handleInput(keys, mouse);
        if (deltaTime > 0) {
            update(deltaTime);
        }
    }

    private void handleInput(KeyHandler keys, MouseHandler mouse) {
        if (keys.enterPressed) {
            keys.enterPressed = false;
            addSpringBody(selectedBody.makeCopy());
            Vector2 node1Position = selectedBody.nodes[0].position;
            for (Node node : springBodies[springBodies.length - 1].nodes) {
                double deltaX = node.position.getX() - node1Position.getX();
                double deltaY = node.position.getY() - node1Position.getY();
                node.position.set(mouse.getX() + deltaX, mouse.getY() + deltaY);
            }
        }

        if (keys.onePressed) {
            keys.onePressed = false;
            selectedBody = DEFAULT_SPRING_BODY;
        }

        if (keys.twoPressed) {
            keys.twoPressed = false;
            selectedBody = CIRCLE_SPRING_BODY;
        }

        if (keys.threePressed) {
            keys.threePressed = false;
            selectedBody = TRIANGLE_SPRING_BODY;
        }

        if (keys.fourPressed) {
            keys.fourPressed = false;
            selectedBody = HEXAGON_SPRING_BODY;
        }

        if (keys.rPressed) {
            keys.rPressed = false;
            resetSpringBodies();
        }

        if (keys.rightPressed) {
            keys.rightPressed = false;
            movement.setX(movement.getX() + 1000);
        }
        if (keys.leftPressed) {
            keys.leftPressed = false;
            movement.setX(movement.getX() - 1000);
        }
        if (keys.upPressed) {
            keys.upPressed = false;
            movement.setY(movement.getY() - 1000);
        }
        if (keys.downPressed) {
            keys.downPressed = false;
            movement.setY(movement.getY() + 1000);
        }
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    public Vector2 getGravity() {
        return this.gravity;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }
    public void setBoundaryFriction(double friction) {
        this.boundary.setFrictionCoefficient(friction);
    }

    public void addSpringBody(SpringBody springBody) {
        this.springBodies = Arrays.copyOf(this.springBodies, this.springBodies.length + 1);
        this.springBodies[this.springBodies.length - 1] = springBody;
        totalNodes += springBody.nodes.length;
        totalSprings += springBody.springs.length;
    }

    public void addStaticObject(StaticObject staticObject) {
        this.staticObjects = Arrays.copyOf(this.staticObjects, this.staticObjects.length + 1);
        this.staticObjects[this.staticObjects.length - 1] = staticObject;
    }

    public void removeSpringBody(SpringBody springBody) {
        totalNodes -= springBody.nodes.length;
        totalSprings -= springBody.springs.length;
        int index = Arrays.asList(this.springBodies).indexOf(springBody);
        if (this.staticObjects.length - index - 1 >= 0)
            System.arraycopy(this.springBodies, index + 1, this.springBodies, index, this.springBodies.length - index - 1);
    }

    public void removeStaticObject(StaticObject staticObject) {
        int index = Arrays.asList(this.staticObjects).indexOf(staticObject);
        if (this.staticObjects.length - index - 1 >= 0)
            System.arraycopy(this.staticObjects, index + 1, this.staticObjects, index, this.staticObjects.length - index - 1);
    }

    public SpringBody[] getSpringBodies() {
        return springBodies;
    }

    public StaticObject[] getStaticObjects() {
        return staticObjects;
    }

    public void resetSpringBodies() {
        this.springBodies = new SpringBody[0];
    }

    public int getTotalNodes() {
        return totalNodes;
    }
    public int getTotalSprings() {
        return totalSprings;
    }

    public void render(Graphics2D g2, double scale, int renderingMode) {
        for (SpringBody body : springBodies) {
            body.render(g2, scale, renderingMode);
        }
        for (StaticObject staticObject : staticObjects) {
            staticObject.render(g2, scale);
        }
    }

}
