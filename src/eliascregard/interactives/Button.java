package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

class Button {

    Color outlineColor;
    Color insideColor;
    String label;
    boolean isPressed;
    Vector2D position;
    boolean mouseIsOver;


    Button(Color outlineColor, Color insideColor, String label, Vector2D position) {
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.label = label;
        this.isPressed = false;
        this.position = position;
        this.mouseIsOver = false;
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        this.isPressed = false;
        if (this.mouseIsOver(mousePosition)) {
            this.mouseIsOver = true;
            if (mouseButton.leftIsPressed) {
                System.out.println("Button pressed");
                mouseButton.leftIsPressed = false;
                this.isPressed = true;
            }
        }
        else {
            this.mouseIsOver = false;
        }
    }

    boolean mouseIsOver(MouseMovementHandler mousePosition) {
        return false;
    }

    public boolean getState() {
        return this.isPressed;
    }

    public void setPosition(double x, double y) {
        this.position = new Vector2D(x, y);
    }
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return this.position;
    }
}
