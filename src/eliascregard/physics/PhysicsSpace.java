package eliascregard.physics;

import eliascregard.input.KeyHandler;
import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class PhysicsSpace {

    private final static SpringBody DEFAULT_SPRING_BODY = SpringBody.homogeneousRectangle(
            0,  0, 20, 2, 1, 2000, 100, 5, 10
    );

    private final static SpringBody CIRCLE_SPRING_BODY = SpringBody.simpleCircle(
            0,  0, 100, 50, 3, 1500, 50, 5
    );

    private final static SpringBody TRIANGLE_SPRING_BODY = SpringBody.homogeneousTriangle(
            0,  0, 15, 1, 2000, 50, 5, 10
    );

    private final static SpringBody HEXAGON_SPRING_BODY = SpringBody.homogeneousHexagon(
            0,  0, 4, 1, 3000, 100, 5, 10
    );

    private Vector2 gravity = new Vector2();
    private LinkedList<SpringBody> springBodies = new LinkedList<>();
    private LinkedList<StaticObject> staticObjects = new LinkedList<>();
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
    }

    private void update(double deltaTime) {
        if (deltaTime > maximumDeltaTime) {
            deltaTime = maximumDeltaTime;
        }
        for (SpringBody body : springBodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                if (boundary != null) {
                    if (node.position.x() > boundary.position.x() + boundary.width || node.position.x() < boundary.position.x()
                        || node.position.y() > boundary.position.y() + boundary.height || node.position.y() < boundary.position.y()) {
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
        if (keys.isKeyJustPressed(KeyEvent.VK_ENTER)) {
            SpringBody newBody = selectedBody.makeCopy();
            spawnSpringBody(newBody, mouse.getPosition());
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_1)) {
            selectedBody = DEFAULT_SPRING_BODY;
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_2)) {
            selectedBody = CIRCLE_SPRING_BODY;
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_3)) {
            selectedBody = TRIANGLE_SPRING_BODY;
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_4)) {
            selectedBody = HEXAGON_SPRING_BODY;
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_R)) {
            resetSpringBodies();
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_RIGHT)) {
            movement.setX(movement.x() + 1000);
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_LEFT)) {
            movement.setX(movement.x() - 1000);
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_UP)) {
            movement.setY(movement.y() - 1000);
        }
        if (keys.isKeyJustPressed(KeyEvent.VK_DOWN)) {
            movement.setY(movement.y() + 1000);
        }
    }

    private void spawnSpringBody(SpringBody springBody, Vector2 position) {
        addSpringBody(springBody);
        Vector2 node1Position = selectedBody.nodes[0].position;
        for (Node node : springBody.nodes) {
            double deltaX = node.position.x() - node1Position.x();
            double deltaY = node.position.y() - node1Position.y();
            node.position.set(position.x() + deltaX, position.y() + deltaY);
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
        springBodies.add(springBody);
        totalNodes += springBody.nodes.length;
        totalSprings += springBody.springs.length;
    }

    public void addStaticObject(StaticObject staticObject) {
        staticObjects.add(staticObject);
    }

    public void removeSpringBody(SpringBody springBody) {
        totalNodes -= springBody.nodes.length;
        totalSprings -= springBody.springs.length;
        springBodies.remove(springBody);
    }

    public void removeStaticObject(StaticObject staticObject) {
        staticObjects.remove(staticObject);
    }

    public SpringBody[] getSpringBodies() {
        return springBodies.toArray(new SpringBody[0]);
    }

    public StaticObject[] getStaticObjects() {
        return staticObjects.toArray(new StaticObject[0]);
    }

    public void resetSpringBodies() {
        springBodies.clear();
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
