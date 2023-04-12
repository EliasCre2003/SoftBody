package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

public class Boundary extends StaticObject{
    Vector2 position;
    double width;
    double height;
    double thickness;

    public Boundary(Vector2 position, double width, double height, double thickness, double restitution, double friction) {
        super(new Vector2[] {
                new Vector2(position.x() + width / 2, position.y() - thickness),
                new Vector2(position.x() + width + thickness, position.y() - thickness),
                new Vector2(position.x() + width + thickness, position.y() + height + thickness),
                new Vector2(position.x() - thickness, position.y() + height + thickness),
                new Vector2(position.x() - thickness, position.y() - thickness),
                new Vector2(position.x() + width / 2, position.y() - thickness),
                new Vector2(position.x() +  width / 2, position.y()),
                new Vector2(position.x(), position.y()),
                new Vector2(position.x(), height),
                new Vector2(position.x() + width, height),
                new Vector2(position.x() + width, position.y()),
                new Vector2(position.x() + width / 2, position.y())
        }, restitution, friction);
        this.position = position;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
    }

}
