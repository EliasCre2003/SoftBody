package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

class Button {

    String label;
    boolean isPressed = false;
    Vector2D position;
    boolean mouseIsOver = false;
    private boolean isVisible = true;

    Button(String label, Vector2D position) {
        this.label = label;
        this.position = position;

    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        this.isPressed = false;
        if (this.mouseIsOver(mousePosition)) {
            this.mouseIsOver = true;
            if (mouseButton.leftIsPressed) {
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
    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }
    public boolean isVisible() {
        return this.isVisible;
    }


}
