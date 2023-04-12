package eliascregard.physics.rigidbody;

import eliascregard.math.vectors.Vector2;

public class Collision {
    
    private static final Box W_BOX_A = new Box();
    private static final Box W_BOX_B = new Box();
    private static final Circle W_CIRCLE_A = new Circle();
    private static final Circle W_CIRCLE_B = new Circle();

    private static final SupportingPointResult SUP_POINT_RESULT_A = new SupportingPointResult();
    private static final SupportingPointResult SUP_POINT_RESULT_B = new SupportingPointResult();

    public static Contact checkCollision(RigidBody bodyA, RigidBody bodyB) {
        if (bodyA.getShape() instanceof Box && bodyB.getShape() instanceof Box) {
            return checkBoxBox(bodyA, bodyB);
        }
        if (bodyA.getShape() instanceof Circle && bodyB.getShape() instanceof Circle) {
            return checkCircleCircle(bodyA, bodyB);
        }
        if (bodyA.getShape() instanceof Circle && bodyB.getShape() instanceof Box) {
            return checkCircleBox(bodyA, bodyB);
        }
        if (bodyA.getShape() instanceof Box && bodyB.getShape() instanceof Circle) {
            return checkCircleBox(bodyB, bodyA);
        }
        return null;
    }
    
    private static Contact checkBoxBox(RigidBody ba, RigidBody bb) {
        if (!(ba.getShape() instanceof Box) || !(bb.getShape() instanceof Box)) {
            return null;
        }
        
        Box boxA = (Box) ba.getShape();
        Box boxB = (Box) bb.getShape();
        
        W_BOX_A.convertToWorldSpace(boxA, ba.getTransform());
        W_BOX_B.convertToWorldSpace(boxB, bb.getTransform());

        getClosestSupportingPoint(W_BOX_A, W_BOX_B, SUP_POINT_RESULT_A);
        getClosestSupportingPoint(W_BOX_B, W_BOX_A, SUP_POINT_RESULT_B);
        
        if (SUP_POINT_RESULT_A.supportingPoint == null || SUP_POINT_RESULT_B.supportingPoint == null) {
            return null;
        }
        
        Vector2 contactPoint = null;
        Vector2 normal = new Vector2();
        double penetration = -Integer.MAX_VALUE;
        
        if (SUP_POINT_RESULT_A.supportingPoint != null) {
            contactPoint = SUP_POINT_RESULT_A.supportingPoint;
            penetration = SUP_POINT_RESULT_A.minDistance;
            normal.set(SUP_POINT_RESULT_A.normal);
        }

        if (SUP_POINT_RESULT_B.supportingPoint != null 
                && SUP_POINT_RESULT_B.minDistance > penetration) {
            
            contactPoint = SUP_POINT_RESULT_B.supportingPoint;
            penetration = SUP_POINT_RESULT_B.minDistance;
            normal.set(SUP_POINT_RESULT_B.normal);
            
            // flip
            RigidBody bTmp = ba;
            ba = bb;
            bb = bTmp;
        }
        
        return new Contact(bb, ba, -penetration, normal, contactPoint);
    }

    private static Contact checkCircleBox(RigidBody bodyA, RigidBody bodyB) {
        if (!(bodyA.getShape() instanceof Circle) || !(bodyB.getShape() instanceof Box)) return null;

        Circle circle = (Circle) bodyA.getShape();
        Box box = (Box) bodyB.getShape();

        W_CIRCLE_A.convertToWorldSpace(circle, bodyA.getTransform());
        W_BOX_B.convertToWorldSpace(box, bodyB.getTransform());

        getClosestSupportingPoint(W_CIRCLE_A, W_BOX_B, SUP_POINT_RESULT_A);
        if (SUP_POINT_RESULT_A.supportingPoint == null) return null;

        Vector2 contactPoint = SUP_POINT_RESULT_A.supportingPoint;
        Vector2 normal = SUP_POINT_RESULT_A.normal;
        double penetration = SUP_POINT_RESULT_A.minDistance;

        return new Contact(bodyB, bodyA, -penetration, normal, contactPoint);
    }

    private static Contact checkCircleCircle(RigidBody bodyA, RigidBody bodyB) {
        if (!(bodyA.getShape() instanceof Circle) || !(bodyB.getShape() instanceof Circle)) return null;

        Circle circleA = (Circle) bodyA.getShape();
        Circle circleB = (Circle) bodyB.getShape();

        W_CIRCLE_A.convertToWorldSpace(circleA, bodyA.getTransform());
        W_CIRCLE_B.convertToWorldSpace(circleB, bodyB.getTransform());

        getClosestSupportingPoint(W_CIRCLE_A, W_CIRCLE_B, SUP_POINT_RESULT_A);
        if (SUP_POINT_RESULT_A.supportingPoint == null) return null;

        Vector2 contactPoint = SUP_POINT_RESULT_A.supportingPoint;
        Vector2 normal = SUP_POINT_RESULT_A.normal;
        double penetration = SUP_POINT_RESULT_A.minDistance;

        // penetration may have to be negative - check if any problem occurs
        return new Contact(bodyB, bodyA, penetration, normal, contactPoint);
    }
    
    private static final SupportingPointResult SUP_POINT_RESULT_TMP = new SupportingPointResult();
    private static final Vector2 V_TMP = new Vector2();
    
    // boxA -> edges / boxB -> vertices
    private static void getClosestSupportingPoint(Box boxA, Box boxB, SupportingPointResult supportingPointResult) {
        supportingPointResult.set(null, null, -Double.MAX_VALUE);
        for (Box.Edge edge : boxA.edges) {
            SUP_POINT_RESULT_TMP.set(null, null, 0);
            Vector2 normal = edge.normal;
            boolean allPositive = true;
            for (Vector2 v : boxB.vertices) {
                V_TMP.set(v);
                V_TMP.subtract(edge.a);
                double distance = normal.dotProduct(V_TMP);
                if (distance < SUP_POINT_RESULT_TMP.minDistance) {
                    allPositive = false;
                    SUP_POINT_RESULT_TMP.set(v, normal, distance);
                }
            }
            // not collides - all vertices dot normal are positive
            if (allPositive) {
                supportingPointResult.set(null, null, 0);
                return;
            }
            else if (SUP_POINT_RESULT_TMP.minDistance > supportingPointResult.minDistance) {
                supportingPointResult.set(SUP_POINT_RESULT_TMP.supportingPoint,
                        SUP_POINT_RESULT_TMP.normal, SUP_POINT_RESULT_TMP.minDistance);
            }
        }
    }

    private static void getClosestSupportingPoint(Circle circle, Box boxB, SupportingPointResult supportingPointResult) {
        supportingPointResult.set(null, null, circle.getRadius());
        for (Vector2 v : boxB.vertices) {
            V_TMP.set(v);
            V_TMP.subtract(circle.getCenter());
            double distance = V_TMP.length() - circle.getRadius();
            if (distance < supportingPointResult.minDistance) {
                supportingPointResult.set(v, V_TMP, distance);
            }
        }

    }

    private static void getClosestSupportingPoint(Circle circle1, Circle circle2, SupportingPointResult supportingPointResult) {
        supportingPointResult.set(null, null, 0);
        Vector2 c1 = circle1.getCenter(), c2 = circle2.getCenter();
        double distance = c1.distance(c2);
        if (distance > circle1.getRadius() + circle2.getRadius()) return;
        Vector2 normal = new Vector2(Vector2.difference(c1, c2));
        normal.set(normal.y(), -normal.x());
        double minDistance = circle1.getRadius() + circle2.getRadius() - distance;
        V_TMP.set(c1);
        supportingPointResult.set(V_TMP, normal, minDistance);
    }
    
    private static class SupportingPointResult {
        Vector2 supportingPoint;
        Vector2 normal;
        double minDistance;
        
        void set(Vector2 supportingPoint, Vector2 normal, double minDistance) {
            this.supportingPoint = supportingPoint;
            this.normal = normal;
            this.minDistance = minDistance;
        }
    }
    
}
