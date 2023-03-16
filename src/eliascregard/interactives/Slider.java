package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

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
    public Vector2 position;
    public Vector2 circlePosition;
    public String name;

    public Slider(
            String name, double startValue, double min, double max, double length, boolean isHorizontal,
            Color circleColor, Color lineColor, Vector2 position
    ) {
        this.name = name;
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

    public Slider(double startValue, double min, double max, boolean isHorizontal, Vector2 position) {
        this("Value", startValue, min, max, 100, isHorizontal, Color.WHITE, Color.WHITE, position);
    }

    public void update(MouseHandler mouse) {
        if (isPressed) {
            if (isHorizontal) {
                if (mouse.getX() < position.getX()) {
                    circlePosition.setX(position.getX());
                } else if (mouse.getX() > position.getX() + length) {
                    circlePosition.setX(position.getX() + length);
                } else {
                    circlePosition.setX(mouse.getX());
                }
            } else {
                if (mouse.getY() < position.getY()) {
                    circlePosition.setY(position.getY());
                } else if (mouse.getY() > position.getY() + length) {
                    circlePosition.setY(position.getY() + length);
                } else {
                    circlePosition.setY(mouse.getY());
                }
            }
            this.value = calculateValue();
        } else if (mouse.leftIsPressed()) {
            this.isPressed = Vector2.distance(circlePosition, new Vector2(mouse.getX(), mouse.getY())) < 10;
        }
        if (!mouse.leftIsPressed()) {
            this.isPressed = false;
        }
    }

    public Vector2 calculateCirclePosition() {
        if (this.isHorizontal) {
            return new Vector2(
                    position.getX() + (value - min) / (max - min) * length,
                    position.getY()
            );
        } else {
            return new Vector2(
                    this.position.getX(),
                    this.position.getY() + (value - min) / (max - min) * length
            );
        }
    }

    public double calculateValue() {
        if (this.isHorizontal) {
            return this.min + (this.circlePosition.getX() - this.position.getX()) / this.length * (this.max - this.min);
        } else {
            return this.min + (this.circlePosition.getY() - this.position.getY()) / this.length * (this.max - this.min);
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(this.lineColor);
        g2.setStroke(new BasicStroke((float) (3*scale)));
        if (this.isHorizontal) {
            g2.drawLine((int) (this.position.getX() * scale), (int) (this.position.getY() * scale), (int) ((this.position.getX() + this.length) * scale), (int) (this.position.getY() * scale));
            g2.drawString(name + ": " + this.value, (int) (this.position.getX() * scale), (int) ((this.position.getY() + 20) * scale));
        } else {
            g2.drawLine((int) (this.position.getX() * scale), (int) (this.position.getY() * scale), (int) (this.position.getX() * scale), (int) ((this.position.getY() + this.length) * scale));
//            g2.drawString("Value: " + this.value, (int) (this.position.x * scale), (int) ((this.position.y + this.length + 20) * scale));
        }
        g2.setColor(this.circleColor);
        g2.fillOval((int) ((this.circlePosition.getX() - 10) * scale), (int) ((this.circlePosition.getY() - 10) * scale), (int) (20 * scale), (int) (20 * scale));
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke((float) (2*scale)));
        g2.drawOval((int) ((this.circlePosition.getX() - 10) * scale), (int) ((this.circlePosition.getY() - 10) * scale), (int) (20 * scale), (int) (20 * scale));
    }

}
