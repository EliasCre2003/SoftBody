package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Switch {

    public Shape shape;
    public Color color;
    public String text;
    public boolean isFilled;
    public boolean isOn;
    public Vector2D position;

    public Switch(Shape shape, Color color, String text, boolean isFilled, Vector2D position) {
        this.shape = shape;
        this.color = color;
        this.text = text;
        this.isFilled = isFilled;
        this.isOn = false;
        this.position = position;

    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        if (mouseButton.pressed) {
            if (this.shape.contains(mousePosition.x, mousePosition.y)) {
                this.isOn = !this.isOn;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        if (this.isFilled) {
            g2.fill(this.shape);
        } else {
            g2.draw(this.shape);
        }
        g2.setColor(Color.BLACK);
        g2.drawString(this.text, (int) this.shape.getBounds().getCenterX(), (int) this.shape.getBounds().getCenterY());
    }

}
