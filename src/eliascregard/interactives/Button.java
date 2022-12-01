package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Button {

    public Color outlineColor;
    public Color insideColor;
    public String label;
    public boolean isPressed;
    public Vector2D position;


    public Button(Color outlineColor, Color insideColor, String label, Vector2D position) {
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.label = label;
        this.isPressed = false;
        this.position = position;
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        this.isPressed = false;
        if (mouseButton.leftIsPressed) {
            if (this.position.distance(new Vector2D(mousePosition.x - 10, mousePosition.y - 10)) < 10) {
                System.out.println("Button pressed");
                mouseButton.leftIsPressed = false;
                this.isPressed = true;
            }
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(this.outlineColor);
        g2.setStroke(new BasicStroke((float) (1*scale)));
        g2.drawOval((int) ((this.position.x) * scale), (int) ((this.position.y) * scale), (int) (20 * scale), (int) (20 * scale));
        g2.setColor(this.insideColor);
        g2.fillOval((int) ((this.position.x + 2.5) * scale), (int) ((this.position.y + 2.5) * scale), (int) (15 * scale), (int) (15 * scale));
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(this.label, (int) ((this.position.x + 25) * scale), (int) ((this.position.y + 15) * scale));
    }
}
