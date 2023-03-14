package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.math.vectors.Vector2D;

import java.awt.*;

public class RectangularButton extends Button {

    private Color outlineColor = null;
    private Color insideColor;
    private final Dimension size;
    private boolean labelIsInside;

    public RectangularButton(String label, Vector2D position, Color outlineColor, Color insideColor, Dimension size,
                             boolean labelIsInside)
    {
        super(label, position);
        this.outlineColor = outlineColor;
        this.insideColor = insideColor;
        this.size = size;
        this.labelIsInside = labelIsInside;
    }
    public RectangularButton(String label, Vector2D position, Color insideColor, Dimension size,
                             boolean labelIsInside)
    {
        super(label, position);
        this.insideColor = insideColor;
        this.size = size;
        this.labelIsInside = labelIsInside;
    }

    public double getWidth() {
        return this.size.getWidth();
    }
    public double getHeight() {
        return this.size.getHeight();
    }
    public void setWidth(int width) {
        this.size.width = width;
    }
    public void setHeight(int height) {
        this.size.height = height;
    }

    public Color getOutlineColor() {
        return this.outlineColor;
    }
    public Color getInsideColor() {
        return this.insideColor;
    }
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
    public void setInsideColor(Color insideColor) {
        this.insideColor = insideColor;
    }



    boolean mouseIsOver(MouseHandler mouse) {
        return mouse.getX() > this.position.x && mouse.getX() < this.position.x + this.size.getWidth()
                && mouse.getY() > this.position.y && mouse.getY() < this.position.y + this.size.getHeight();
    }


    public void render(Graphics2D g2, double scale) {
        if (!this.isVisible()) {
            return;
        }
        double colorMultiplier = 1;
        if (this.mouseIsOver) {
            colorMultiplier = 0.8;
        }
        if (this.outlineColor != null) {
            g2.setColor(new Color((int) (this.outlineColor.getRed() * colorMultiplier), (int) (this.outlineColor.getGreen() * colorMultiplier), (int) (this.outlineColor.getBlue() * colorMultiplier)));
            g2.setStroke(new BasicStroke((float) (1 * scale)));
            g2.drawRect((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.size.getWidth() * scale), (int) (this.size.getHeight() * scale));
        }
        g2.setColor(new Color((int) (this.insideColor.getRed() * colorMultiplier), (int) (this.insideColor.getGreen() * colorMultiplier), (int) (this.insideColor.getBlue() * colorMultiplier)));
        g2.fillRect((int) ((this.position.x + 2.5) * scale), (int) ((this.position.y + 2.5) * scale), (int) ((this.size.getWidth() - 5) * scale), (int) ((this.size.getHeight() - 5) * scale));
        g2.setColor(new Color((int)(255*colorMultiplier), (int)(255*colorMultiplier), (int)(255*colorMultiplier)));
        if (this.labelIsInside) {
            g2.drawString(this.label, (int) ((this.position.x + 5) * scale), (int) ((this.position.y + this.size.getHeight() / 2 + 5) * scale));
        }
        else {
            g2.drawString(this.label, (int) ((this.position.x + this.size.getWidth() + 5) * scale), (int) ((this.position.y + this.size.getHeight() / 2 + 5) * scale));
        }

    }
}
