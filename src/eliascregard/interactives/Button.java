package eliascregard.interactives;

import eliascregard.physics.Vector2D;

import java.awt.*;

public class Button {

    public Shape shape;
    public Color color;
    public String text;
    public boolean isFilled;
    public boolean isPressed;


    public Button(Shape shape, Color color, String text, boolean isFilled) {
        this.shape = shape;
        this.color = color;
        this.isPressed = false;
        this.text = text;
        this.isFilled = isFilled;
    }

    public void update(Vector2D mousePosition) {
        this.isPressed = this.shape.contains(mousePosition.x, mousePosition.y);
    }
}
