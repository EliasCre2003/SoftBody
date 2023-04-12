package eliascregard.physics.rigidbody;

import eliascregard.math.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;

public class Contact {

    public RigidBody bodyA;
    public RigidBody bodyB;
    public double penetration;
    public final Vector2 point = new Vector2();
    public final Vector2 normal = new Vector2();

    public Contact(RigidBody bodyA, RigidBody bodyB, double penetration, Vector2 normal, Vector2 point) {
        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.penetration = penetration;
        this.normal.set(normal);
        this.point.set(point);
    }

    public RigidBody getBodyA() {
        return bodyA;
    }

    public RigidBody getBodyB() {
        return bodyB;
    }

    public double getPenetration() {
        return penetration;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void resolveCollision() {
        // vap1
        Vector2 vap1 = new Vector2();
        vap1.set(bodyA.velocity);

        Vector2 r1 = new Vector2();
        r1.set(point);
        r1.subtract(bodyA.position);

        Vector2 val1 = new Vector2();
        val1.crossProduct(r1);
        val1.scale(bodyA.angularVelocity);
        vap1.add(val1);
        
        // vap2
        Vector2 vap2 = new Vector2();
        vap2.set(bodyB.velocity);

        Vector2 r2 = new Vector2();
        r2.set(point);
        r2.subtract(bodyB.position);

        Vector2 val2 = new Vector2();
        val2.crossProduct(r2);
        val2.scale(bodyB.angularVelocity);
        vap2.add(val2);
        
        // relative velocity
        Vector2 vrel = new Vector2();
        vrel.set(vap1);
        vrel.subtract(vap2);
        
        if (vrel.dotProduct(normal) > 0) {
            return;
        }
        
        double e = 0.5; // restitution hard coded
        
        double raxn = r1.crossProduct(normal);
        double rbxn = r2.crossProduct(normal);
        
        double totalMass = (1 / bodyA.mass) + (1 / bodyB.mass) + ((raxn * raxn) / bodyA.inertia) + ((rbxn * rbxn) / bodyB.inertia);
        
        double j = (-(1 + e) * vrel.dotProduct(normal)) / totalMass;
        
        Vector2 impulse = new Vector2();
        impulse.set(normal);
        impulse.scale(j);
        
        bodyA.applyImpulse(impulse, point);
        impulse.scale(-1);
        bodyB.applyImpulse(impulse, point);
    }
    
    public void correctPosition() {
        double f = penetration / (bodyA.mass + bodyB.mass);
        Vector2 p = new Vector2();
        
        p.set(normal);
        p.scale(f * bodyB.mass * 0.8);
        bodyA.position.add(p);
        
        p.set(normal);
        p.scale(f * bodyA.mass * 0.8);
        bodyB.position.subtract(p);
    }
    
    public void drawDebug(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawLine((int) point.x(), (int) point.y(), (int) (point.x() + normal.x() * penetration), (int) (point.y() + normal.y() * penetration));
        g.setColor(Color.RED);
        g.fillOval((int) (point.x() - 4), (int) (point.y() - 4), 8, 8);
    }

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact c = (Contact) obj;
        if (bodyA == c.bodyA && bodyB == c.bodyB) {
            return true;
        }
        return bodyA == c.bodyB && bodyB == c.bodyA;
    }
}
