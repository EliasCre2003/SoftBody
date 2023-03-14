package eliascregard.physics;

import eliascregard.input.KeyHandler;
import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.math.vectors.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class PhysicsSpace {

    private final static SpringBody DEFAULT_SPRING_BODY = SpringBody.homogeneousRectangle(
            800,  0, 20, 2, 1, 2000, 100, 5, 10
    );

    private final static SpringBody CIRCLE_SPRING_BODY = SpringBody.homogeneousCircle(
            800,  300, 100, 25, 3, 1500, 50, 10
    );

    private final static SpringBody TRIANGLE_SPRING_BODY = SpringBody.homogeneousTriangle(
            800,  300, 30, 1, 20000, 200, 5, 10
    );

    private Vector2D gravity = new Vector2D();
    private SpringBody[] springBodies = new SpringBody[0];
    private StaticObject[] staticObjects = new StaticObject[0];
    private Boundary boundary = null;
    private final Vector2D movement = new Vector2D();
    private int totalNodes = 0;
    private int totalSprings = 0;
    private SpringBody selectedBody = DEFAULT_SPRING_BODY;
    private final double maximumDeltaTime = 1.0 / 1000;


    public PhysicsSpace() {
    }

    public PhysicsSpace(Vector2D gravity) {
        this.gravity = gravity;
    }

    private void update(double deltaTime) {
        if (deltaTime > maximumDeltaTime) {
            deltaTime = maximumDeltaTime;
        }
        for (SpringBody body : springBodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                if (boundary != null) {
                    if (node.position.x > boundary.position.x + boundary.width || node.position.x < boundary.position.x
                            || node.position.y > boundary.position.y + boundary.height || node.position.y < boundary.position.y) {
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
            Vector2D node1Position = selectedBody.nodes[0].position;
            for (Node node : springBodies[springBodies.length - 1].nodes) {
                double deltaX = node.position.x - node1Position.x;
                double deltaY = node.position.y - node1Position.y;
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

        if (keys.rPressed) {
            keys.rPressed = false;
            resetSpringBodies();
        }

        if (keys.rightPressed) {
            keys.rightPressed = false;
            movement.x += 1000;
        }
        if (keys.leftPressed) {
            keys.leftPressed = false;
            movement.x -= 1000;
        }
        if (keys.upPressed) {
            keys.upPressed = false;
            movement.y -= 1000;
        }
        if (keys.downPressed) {
            keys.downPressed = false;
            movement.y += 1000;
        }
    }

    public void setGravity(Vector2D gravity) {
        this.gravity = gravity;
    }

    public Vector2D getGravity() {
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
