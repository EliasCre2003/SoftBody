package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

public class Boundary extends StaticObject{
    Vector2 position;
    double width;
    double height;
    double thickness;

    public Boundary(Vector2 position, double width, double height, double thickness, double restitution, double friction) {
        super(new Vector2[] {
                new Vector2(position.getX() + width / 2, position.getY() - thickness),
                new Vector2(position.getX() + width + thickness, position.getY() - thickness),
                new Vector2(position.getX() + width + thickness, position.getY() + height + thickness),
                new Vector2(position.getX() - thickness, position.getY() + height + thickness),
                new Vector2(position.getX() - thickness, position.getY() - thickness),
                new Vector2(position.getX() + width / 2, position.getY() - thickness),
                new Vector2(position.getX() +  width / 2, position.getY()),
                new Vector2(position.getX(), position.getY()),
                new Vector2(position.getX(), height),
                new Vector2(position.getX() + width, height),
                new Vector2(position.getX() + width, position.getY()),
                new Vector2(position.getX() + width / 2, position.getY())
        }, restitution, friction);
        this.position = position;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
    }

}
