package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class Switch {
    public Color outlineColor;
    public Color insideColor;
    public String label;
    public boolean isOn;
    public Vector2 position;

    public Switch(Color outlineColor, Color insideColor, String label, Vector2 position) {
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.label = label;
        this.isOn = false;
        this.position = position;

    }

    public void update(MouseHandler mouse) {
        if (mouse.leftIsPressed()) {
            if (Vector2.distance(position, new Vector2(mouse.getX() - 10, mouse.getY() - 10)) < 10) {
                mouse.setLeftIsPressed(false);
                this.isOn = !this.isOn;
            }
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(this.outlineColor);
        g2.setStroke(new BasicStroke((float) (1*scale)));
        g2.drawOval((int) ((this.position.getX()) * scale), (int) ((this.position.getY()) * scale), (int) (20 * scale), (int) (20 * scale));
        if (this.isOn) {
            g2.setColor(this.insideColor);
            g2.fillOval((int) ((this.position.getX() + 2.5) * scale), (int) ((this.position.getY() + 2.5) * scale), (int) (15 * scale), (int) (15 * scale));
        }
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(this.label, (int) ((this.position.getX() + 25) * scale), (int) ((this.position.getY() + 15) * scale));
    }

}
