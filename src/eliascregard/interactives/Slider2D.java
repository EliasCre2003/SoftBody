package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.math.vectors.Vector2;

import java.awt.*;

public class Slider2D {

    public Vector2 value;
    public double xMax;
    public double xMin;
    public double yMax;
    public double yMin;
    public Dimension size;
    public boolean isPressed;
    public Color circleColor;
    public Color frameColor;

    public Vector2 position;
    public Vector2 circlePosition;

    public Slider2D(
            Vector2 startValue, double xMin, double xMax, double yMin, double yMax, Dimension size,
            Color circleColor, Color frameColor, Vector2 position
    ) {
        this.value = startValue;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.isPressed = false;
        this.circleColor = circleColor;
        this.frameColor = frameColor;
        this.position = position;
        this.circlePosition = calculateCirclePosition();
    }

    private Vector2 calculateCirclePosition() {
        double x = this.position.getX() + (this.value.getX() - this.xMin) / (this.xMax - this.xMin) * this.size.width;
        double y = this.position.getY() + (this.value.getY() - this.yMin) / (this.yMax - this.yMin) * this.size.height;
        return new Vector2(x, y);
    }

    private Vector2 calculateValue() {
        double x = this.xMin + (this.circlePosition.getX() - this.position.getX()) / this.size.width * (this.xMax - this.xMin);
        double y = this.yMin + (this.circlePosition.getY() - this.position.getY()) / this.size.height * (this.yMax - this.yMin);
        return new Vector2(x, y);
    }

    public void setValue(Vector2 value) {
        this.value = value;
        this.circlePosition = calculateCirclePosition();
    }

    public void update(MouseHandler mouse) {
        if (this.isPressed) {
            if (mouse.getX() < this.position.getX()) {
                this.circlePosition.setX(position.getX());
            } else if (mouse.getX() > this.position.getX() + this.size.width) {
                this.circlePosition.setX(this.position.getX() + this.size.width);
            } else {
                this.circlePosition.setX(mouse.getX());
            }
            if (mouse.getY() < this.position.getY()) {
                this.circlePosition.setY(this.position.getY());
            } else if (mouse.getY() > this.position.getY() + this.size.height) {
                this.circlePosition.setY(this.position.getY() + this.size.height);
            } else {
                this.circlePosition.setY(mouse.getY());
            }
            this.value = calculateValue();
        } else if (mouse.leftIsPressed()) {
            this.isPressed = Vector2.distance(circlePosition, new Vector2(mouse.getX(), mouse.getY())) < 10;
        }
        if (!mouse.leftIsPressed()) {
            this.isPressed = false;
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(0, 0, 0, 0.5f));
        g2.fillRect((int) (this.position.getX() * scale), (int) (this.position.getY() * scale), (int) (this.size.width * scale), (int) (this.size.height * scale));
//        g2.setColor(this.frameColor);
//        g2.drawRect((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.size.width * scale), (int) (this.size.height * scale));
        g2.setColor(this.circleColor);
        g2.fillOval((int) ((this.circlePosition.getX() - 10) * scale), (int) ((this.circlePosition.getY() - 10) * scale), (int) (20 * scale), (int) (20 * scale));
    }

}
