package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Slider {

    public double value;
    public double min;
    public double max;
    public double length;
    public boolean isPressed;
    public boolean isHorizontal;
    public Color circleColor;
    public Color lineColor;
    public Vector2D position;
    public Vector2D circlePosition;

    public Slider(
            double startValue, double min, double max, double length, boolean isHorizontal,
            Color circleColor, Color lineColor, Vector2D position
    ) {
        this.value = startValue;
        this.min = min;
        this.max = max;
        this.length = length;
        this.isPressed = false;
        this.isHorizontal = isHorizontal;
        this.circleColor = circleColor;
        this.lineColor = lineColor;
        this.position = position;
        this.circlePosition = calculateCirclePosition();
    }

    public Slider(double startValue, double min, double max, boolean isHorizontal, Vector2D position) {
        this(startValue, min, max, 100, isHorizontal, Color.WHITE, Color.WHITE, position);
    }

    public void update(MouseHandler mouseButton, MouseMovementHandler mousePosition) {
        if (this.isPressed) {
            if (this.isHorizontal) {
                if (mousePosition.x < this.position.x) {
                    this.circlePosition.x = this.position.x;
                } else if (mousePosition.x > this.position.x + this.length) {
                    this.circlePosition.x = this.position.x + this.length;
                } else {
                    this.circlePosition.x = mousePosition.x;
                }
                if (this.circlePosition.x > this.position.x + this.length) {
                    this.circlePosition.x = this.position.x + this.length;
                } else if (this.circlePosition.x < this.position.x) {
                    this.circlePosition.x = this.position.x;
                }
            } else {
                if (mousePosition.y < this.position.y) {
                    this.circlePosition.y = this.position.y;
                } else if (mousePosition.y > this.position.y + this.length) {
                    this.circlePosition.y = this.position.y + this.length;
                } else {
                    this.circlePosition.y = mousePosition.y;
                }
                if (this.circlePosition.y > this.position.y + this.length) {
                    this.circlePosition.y = this.position.y + this.length;
                } else if (this.circlePosition.y < this.position.y) {
                    this.circlePosition.y = this.position.y;
                }
            }
            this.value = calculateValue();
        } else if (mouseButton.pressed) {
            this.isPressed = this.circlePosition.distance(new Vector2D(mousePosition.x, mousePosition.y)) < 10;
        }
        if (!mouseButton.pressed) {
            this.isPressed = false;
        }
    }

    public Vector2D calculateCirclePosition() {
        if (this.isHorizontal) {
            return new Vector2D(
                    this.position.x + (this.value - this.min) / (this.max - this.min) * this.length,
                    this.position.y
            );
        } else {
            return new Vector2D(
                    this.position.x,
                    this.position.y + (this.value - this.min) / (this.max - this.min) * this.length
            );
        }
    }

    public double calculateValue() {
        if (this.isHorizontal) {
            return this.min + (this.circlePosition.x - this.position.x) / this.length * (this.max - this.min);
        } else {
            return this.min + (this.circlePosition.y - this.position.y) / this.length * (this.max - this.min);
        }
    }

    public void draw(Graphics2D g2, double scale) {
        g2.setColor(this.lineColor);
        g2.setStroke(new BasicStroke((float) (3*scale)));
        if (this.isHorizontal) {
            g2.drawLine((int) (this.position.x * scale), (int) (this.position.y * scale), (int) ((this.position.x + this.length) * scale), (int) (this.position.y * scale));
            g2.drawString("Value: " + this.value, (int) (this.position.x * scale), (int) ((this.position.y + 20) * scale));
        } else {
            g2.drawLine((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.position.x * scale), (int) ((this.position.y + this.length) * scale));
//            g2.drawString("Value: " + this.value, (int) (this.position.x * scale), (int) ((this.position.y + this.length + 20) * scale));
        }
        g2.setColor(this.circleColor);
        g2.fillOval((int) ((this.circlePosition.x - 10) * scale), (int) ((this.circlePosition.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke((float) (2*scale)));
        g2.drawOval((int) ((this.circlePosition.x - 10) * scale), (int) ((this.circlePosition.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
    }

}
