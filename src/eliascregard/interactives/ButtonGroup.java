package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.math.vectors.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class ButtonGroup {

    private Vector2D position;
    private Button[] buttons = new Button[0];
    private boolean isVisible = true;

    public ButtonGroup(Vector2D position) {
        this.position = position;
    }

    public void addButton(Button button) {
        button.position.add(this.position);
        button.setVisibility(this.isVisible);
        buttons = Arrays.copyOf(buttons, buttons.length + 1);
        buttons[buttons.length - 1] = button;
    }
    public void addButtons(Button[] buttons) {
        this.buttons = Arrays.copyOf(this.buttons, this.buttons.length + buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].position.add(this.position);
            buttons[i].setVisibility(this.isVisible);
            this.buttons[this.buttons.length - buttons.length + i] = buttons[i];
        }
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        for (Button button : buttons) {
            button.update(mouseButton, mousePosition);
        }
    }

    public void setPosition(Vector2D position) {
        for (Button button : buttons) {
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

    public Button getButton(int index) {
        return this.buttons[index];
    }

    public void setVisibility(boolean isVisible) {
        for (Button button : this.buttons) {
            button.setVisibility(isVisible);
        }
        this.isVisible = isVisible;
    }
    public boolean isVisible() {
        return isVisible;
    }

    public void render(Graphics2D g2, double scale) {
        for (Button button : this.buttons) {
            if (button instanceof CircularButton) {
                ((CircularButton) button).render(g2, scale);
            } else if (button instanceof RectangularButton) {
                ((RectangularButton) button).render(g2, scale);
            }
        }
    }

}
