package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Switch {
    public Color outlineColor;
    public Color insideColor;
    public String label;
    public boolean isOn;
    public Vector2D position;

    public Switch(Color outlineColor, Color insideColor, String label, Vector2D position) {
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.label = label;
        this.isOn = false;
        this.position = position;

    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        if (mouseButton.leftIsPressed) {
            if (this.position.distance(new Vector2D(mousePosition.x, mousePosition.y)) < 10) {
                mouseButton.leftIsPressed = false;
                this.isOn = !this.isOn;
            }
        }
    }

    public void draw(Graphics2D g2, double scale) {
        g2.setColor(this.outlineColor);
        g2.drawOval((int) ((this.position.x - 10) * scale), (int) ((this.position.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
        if (this.isOn) {
            g2.setColor(this.insideColor);
            g2.fillOval((int) ((this.position.x - 10) * scale), (int) ((this.position.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
        }
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(this.label, (int) ((this.position.x + 15) * scale), (int) ((this.position.y + 5) * scale));
    }

}
