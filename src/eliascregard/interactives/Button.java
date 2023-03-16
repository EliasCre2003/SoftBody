package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

public class Button {

    String label;
    boolean isPressed = false;
    Vector2 position;
    boolean mouseIsOver = false;
    public boolean isVisible = true;

    Button(String label, Vector2 position) {
        this.label = label;
        this.position = position;
    }

    public void update(MouseHandler mouse) {
        isPressed = false;
        if (this.mouseIsOver(mouse)) {
            mouseIsOver = true;
            if (mouse.leftIsPressed()) {
                mouse.setLeftIsPressed(false);
                isPressed = true;
            }
        }
        else {
            mouseIsOver = false;
        }
    }

    boolean mouseIsOver(MouseHandler mouse) {
        return false;
    }

    public boolean getState() {
        return this.isPressed;
    }

    public void setPosition(double x, double y) {
        this.position = new Vector2(x, y);
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getPosition() {
        return this.position;
    }
    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }
    public boolean isVisible() {
        return isVisible;
    }


}
