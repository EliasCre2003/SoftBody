package eliascregard.interactives;

import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class RectangularButton extends Button {

    Dimension size;

    public RectangularButton(Color outlineColor, Color insideColor, String label, Vector2D position, Dimension size) {
        super(outlineColor, insideColor, label, position);
        this.size = size;
    }

    public double getWidth() {
        return this.size.getWidth();
    }
    public double getHeight() {
        return this.size.getHeight();
    }

    boolean mouseIsOver(MouseMovementHandler mousePosition) {
        return mousePosition.x > this.position.x && mousePosition.x < this.position.x + this.size.getWidth()
                && mousePosition.y > this.position.y && mousePosition.y < this.position.y + this.size.getHeight();
    }


    public void render(Graphics2D g2, double scale) {
        g2.setColor(this.outlineColor);
        g2.setStroke(new BasicStroke((float) (1*scale)));
        g2.drawRect((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.size.getWidth() * scale), (int) (this.size.getHeight() * scale));
        g2.setColor(this.insideColor);
        g2.fillRect((int) ((this.position.x + 2.5) * scale), (int) ((this.position.y + 2.5) * scale), (int) ((this.size.getWidth() - 5) * scale), (int) ((this.size.getHeight() - 5) * scale));
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(this.label, (int) ((this.position.x + this.size.getWidth() + 5) * scale), (int) ((this.position.y + this.size.getHeight() / 2 + 5) * scale));
    }
}
