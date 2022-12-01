package eliascregard.physics;

import eliascregard.input.KeyHandler;
import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;

import java.awt.*;
import java.util.Arrays;

public class PhysicsSpace {

    private final static SpringBody DEFAULT_SPRING_BODY = SpringBody.homogeneousRectangle(
            800,  0, 8, 8, 1, 1000, 100, 5, 10
    );

    private Vector2D gravity = new Vector2D();
    private SpringBody[] springBodies = new SpringBody[0];
    private StaticObject[] staticObjects = new StaticObject[0];
    private StaticObject boundary = null;
    private final Vector2D movement = new Vector2D();

    public PhysicsSpace() {
    }

    public PhysicsSpace(Vector2D gravity) {
        this.gravity = gravity;
    }

    public void update(double deltaTime) {
        for (SpringBody body : springBodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                if (boundary != null && node.insidePerimeter(boundary)) {
                    node.resolveCollision(boundary);
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

    public void update(double deltaTime, KeyHandler keys, MouseButtonHandler mouseButtons, MouseMovementHandler mouseMovement) {
        handleInput(keys, mouseButtons, mouseMovement);
        if (deltaTime > 0) {
            update(deltaTime);
        }
    }

    private void handleInput(KeyHandler keys, MouseButtonHandler mouseButtons, MouseMovementHandler mouseMovement) {
        if (keys.enterPressed) {
            keys.enterPressed = false;
            addSpringBody(DEFAULT_SPRING_BODY.makeCopy());
            Vector2D node1Position = DEFAULT_SPRING_BODY.nodes[0].position;
            for (Node node : springBodies[springBodies.length - 1].nodes) {
                double deltaX = node.position.x - node1Position.x;
                double deltaY = node.position.y - node1Position.y;
                node.position.set(mouseMovement.x + deltaX, mouseMovement.y + deltaY);
            }
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

    public void setBoundary(StaticObject boundary) {
        this.boundary = boundary;
    }

    public void addSpringBody(SpringBody springBody) {
        this.springBodies = Arrays.copyOf(this.springBodies, this.springBodies.length + 1);
        this.springBodies[this.springBodies.length - 1] = springBody;
    }

    public void addStaticObject(StaticObject staticObject) {
        this.staticObjects = Arrays.copyOf(this.staticObjects, this.staticObjects.length + 1);
        this.staticObjects[this.staticObjects.length - 1] = staticObject;
    }

    public void removeSpringBody() {
        this.springBodies = Arrays.copyOf(this.springBodies, this.springBodies.length - 1);
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

    public void draw(Graphics2D g2, double scale, int renderingMode) {
        for (SpringBody body : springBodies) {
            body.draw(g2, scale, renderingMode);
        }
        for (StaticObject staticObject : staticObjects) {
            staticObject.draw(g2, scale);
        }
    }


}
