package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class ButtonGroup {

    private Vector2D position;

    public CircularButton[] circularButtons = new CircularButton[0];
    public RectangularButton[] rectangularButtons = new RectangularButton[0];
    private boolean isVisible = true;

    public ButtonGroup(Vector2D position) {
        this.position = position;
    }

    public void addButton(CircularButton button) {
        button.position.add(this.position);
        button.setVisibility(this.isVisible);
        this.circularButtons = Arrays.copyOf(this.circularButtons, this.circularButtons.length + 1);
        this.circularButtons[this.circularButtons.length - 1] = button;
    }
    public void addButton(RectangularButton button) {
        button.position.add(this.position);
        button.setVisibility(this.isVisible);
        this.rectangularButtons = Arrays.copyOf(this.rectangularButtons, this.rectangularButtons.length + 1);
        this.rectangularButtons[this.rectangularButtons.length - 1] = button;
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {

        for (CircularButton button : this.circularButtons) {
            button.update(mouseButton, mousePosition);
        }
        for (RectangularButton button : this.rectangularButtons) {
            button.update(mouseButton, mousePosition);
        }
    }

    public void setPosition(Vector2D position) {
        for (CircularButton button : this.circularButtons) {
            button.position.add(Vector2D.difference(position, this.position));
        }
        for (RectangularButton button : this.rectangularButtons) {
            button.position.add(Vector2D.difference(position, this.position));
        }
        this.position = position;
    }
    public void setPosition(double x, double y) {
        this.setPosition(new Vector2D(x, y));
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setVisibility(boolean isVisible) {
        for (CircularButton button : this.circularButtons) {
            button.setVisibility(isVisible);
        }
        for (RectangularButton button : this.rectangularButtons) {
            button.setVisibility(isVisible);
        }
        this.isVisible = isVisible;
    }
    public boolean isVisible() {
        return this.isVisible;
    }

    public void render(Graphics2D g2, double scale) {
        for (CircularButton button : this.circularButtons) {
            button.render(g2, scale);
        }
        for (RectangularButton button : this.rectangularButtons) {
            button.render(g2, scale);
        }
    }

}
