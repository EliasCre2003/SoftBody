package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class CircularButton extends Button {

    private double radius;
    private Color outlineColor;
    private Color insideColor;

    public CircularButton(String label, Vector2 position, Color outlineColor, Color insideColor, double radius) {
        super(label, position);
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.radius = radius;
    }

    boolean mouseIsOver(MouseHandler mouse) {
        return Vector2.distance(position, new Vector2(mouse.getX() - radius, mouse.getY() - radius)) < this.radius;
    }

    public double getRadius() {
        return this.radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
    public void setInsideColor(Color insideColor) {
        this.insideColor = insideColor;
    }
    public Color getOutlineColor() {
        return this.outlineColor;
    }
    public Color getInsideColor() {
        return this.insideColor;
    }


    public void render(Graphics2D g2, double scale) {
        if (!this.isVisible()) {
            return;
        }
        double colorMultiplier = 1;
        if (this.mouseIsOver) {
            colorMultiplier = 0.8;
        }
        g2.setColor(new Color((int) (this.outlineColor.getRed() * colorMultiplier), (int) (this.outlineColor.getGreen() * colorMultiplier), (int) (this.outlineColor.getBlue() * colorMultiplier)));
        g2.setStroke(new BasicStroke((float) (1*scale)));
        g2.drawOval((int) ((this.position.getX()) * scale), (int) ((this.position.getY()) * scale), (int) (this.radius * 2 * scale), (int) (this.radius * 2 * scale));
        g2.setColor(new Color((int) (this.insideColor.getRed() * colorMultiplier), (int) (this.insideColor.getGreen() * colorMultiplier), (int) (this.insideColor.getBlue() * colorMultiplier)));
        g2.fillOval((int) ((this.position.getX() + 2.5) * scale), (int) ((this.position.getY() + 2.5) * scale), (int) ((this.radius * 2 - 5) * scale), (int) ((this.radius * 2 - 5) * scale));
        g2.setColor(new Color((int)(255*colorMultiplier), (int)(255*colorMultiplier), (int)(255*colorMultiplier)));
        g2.drawString(this.label, (int) ((this.position.getX() + this.radius*2 + 5) * scale), (int) ((this.position.getY() + this.radius + 5) * scale));
    }

}