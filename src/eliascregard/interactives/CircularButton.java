package eliascregard.interactives;

import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class CircularButton extends Button {

    private double radius;

    public CircularButton(Color outlineColor, Color insideColor, String label, Vector2D position, double radius) {
        super(outlineColor, insideColor, label, position);
        this.radius = radius;
    }

    boolean mouseIsOver(MouseMovementHandler mousePosition) {
        return this.position.distance(new Vector2D(mousePosition.x - radius, mousePosition.y - radius)) < this.radius;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    public void render(Graphics2D g2, double scale) {
        double colorMultiplier = 1;
        if (this.mouseIsOver) {
            colorMultiplier = 0.8;
        }
        g2.setColor(new Color((int) (this.outlineColor.getRed() * colorMultiplier), (int) (this.outlineColor.getGreen() * colorMultiplier), (int) (this.outlineColor.getBlue() * colorMultiplier)));
        g2.setStroke(new BasicStroke((float) (1*scale)));
        g2.drawOval((int) ((this.position.x) * scale), (int) ((this.position.y) * scale), (int) (this.radius * 2 * scale), (int) (this.radius * 2 * scale));
        g2.setColor(new Color((int) (this.insideColor.getRed() * colorMultiplier), (int) (this.insideColor.getGreen() * colorMultiplier), (int) (this.insideColor.getBlue() * colorMultiplier)));
        g2.fillOval((int) ((this.position.x + 2.5) * scale), (int) ((this.position.y + 2.5) * scale), (int) ((this.radius * 2 - 5) * scale), (int) ((this.radius * 2 - 5) * scale));
        g2.setColor(new Color((int)(255*colorMultiplier), (int)(255*colorMultiplier), (int)(255*colorMultiplier)));
        g2.drawString(this.label, (int) ((this.position.x + this.radius*2 + 5) * scale), (int) ((this.position.y + this.radius + 5) * scale));
    }

}