package eliascregard.physics.rigidbody;

import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class RigidBody {

    public final Shape shape = new Box(0, 0);
    public final AffineTransform transform = new AffineTransform();

    public double mass = 1;
    public double inertia = calculateMomentOfInertia(mass, 1, 1);;

    public final Vector2 position = new Vector2();

    public final Vector2 velocity = new Vector2();
    public final Vector2 acceleration = new Vector2();
    public final Vector2 force = new Vector2();

    public double angle = 0;
    public double angularVelocity = 0;
    public double angularAcceleration = 0;
    public double torque = 0;

    public final Vector2 vTmp = new Vector2();
    public final Point2D pTmp = new Point2D.Double();

    private static double calculateMomentOfInertia(double mass, double width, double height) {
        return mass * (width * width + height * height) / 12;
    }

    public Shape getShape() {
        return shape;
    }

    public Vector2 getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public double getMass() {
        return mass;
    }

    public double getInertia() {
        return inertia;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getForce() {
        return force;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public double getTorque() {
        return torque;
    }
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }
    public void setAcceleration(Vector2 acceleration) {
        this.acceleration.set(acceleration);
    }
    public void setForce(Vector2 force) {
        this.force.set(force);
    }
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }
    public void setTorque(double torque) {
        this.torque = torque;
    }
    public void setMass(double mass) {
        this.inertia *= mass / this.mass;
        this.mass = mass;
    }

    public void setDimensions(double width, double height) {
        this.shape.setShape(new Box(width, height));
        this.inertia = calculateMomentOfInertia(mass, width, height);
    }

    public AffineTransform getTransform() {
        transform.setToIdentity();
        transform.translate(position.x(), position.y());
        transform.rotate(angle);
        return transform;
    }

    public AffineTransform getInvertTransform() {
        transform.setToIdentity();
        transform.rotate(-angle);
        transform.translate(-position.x(), -position.y());
        return transform;
    }

    public void applyForce(Vector2 f, Vector2 worldSpacePosition) {
        // linear
        this.force.add(f);

        // angular
        vTmp.set(worldSpacePosition);
        vTmp.subtract(position);
        applyTorque(vTmp.crossProduct(f));
    }

    public void applyAcceleration(Vector2 a) {
        this.acceleration.add(a);
    }

    public void applyTorque(double torque) {
        this.torque += torque;
    }

    public void applyImpulse(Vector2 impulse, Vector2 worldSpacePosition) {
        // linear
        vTmp.set(impulse);
        vTmp.scale(1 / mass);
        velocity.add(vTmp);

        // angular
        vTmp.set(worldSpacePosition);
        vTmp.subtract(position);
        angularVelocity += (vTmp.crossProduct(impulse) / inertia);
    }

    public void update(double deltaTime) {
        position.add(velocity.scaled(deltaTime));
        velocity.add(acceleration.scaled(deltaTime));
        // force.add(View.gravity);
        force.scale(1 / mass);
        acceleration.set(force);

        angle += angularVelocity * deltaTime;
        if (angle > Math.PI) {
            angle -= Math.PI * 2;
        } else if (angle < -Math.PI) {
            angle += Math.PI * 2;
        }
        angularVelocity += angularAcceleration * deltaTime;
        angularAcceleration = torque / inertia;

        force.set(0, 0);
        torque = 0;
    }

    public void drawDebug(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x(), position.y());
        g.rotate(angle);
        shape.drawDebug(g);
        g.setTransform(at);
    }

}