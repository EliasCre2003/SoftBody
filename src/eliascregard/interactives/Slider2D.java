package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;
import eliascregard.math.vectors.Vector2D;

import java.awt.*;

public class Slider2D {

    public Vector2D value;
    public double xMax;
    public double xMin;
    public double yMax;
    public double yMin;
    public Dimension size;
    public boolean isPressed;
    public Color circleColor;
    public Color frameColor;

    public Vector2D position;
    public Vector2D circlePosition;

    public Slider2D(
            Vector2D startValue, double xMin, double xMax, double yMin, double yMax, Dimension size,
            Color circleColor, Color frameColor, Vector2D position
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

    private Vector2D calculateCirclePosition() {
        double x = this.position.x + (this.value.x - this.xMin) / (this.xMax - this.xMin) * this.size.width;
        double y = this.position.y + (this.value.y - this.yMin) / (this.yMax - this.yMin) * this.size.height;
        return new Vector2D(x, y);
    }

    private Vector2D calculateValue() {
        double x = this.xMin + (this.circlePosition.x - this.position.x) / this.size.width * (this.xMax - this.xMin);
        double y = this.yMin + (this.circlePosition.y - this.position.y) / this.size.height * (this.yMax - this.yMin);
        return new Vector2D(x, y);
    }

    public void setValue(Vector2D value) {
        this.value = value;
        this.circlePosition = calculateCirclePosition();
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        if (this.isPressed) {
            if (mousePosition.x < this.position.x) {
                this.circlePosition.x = this.position.x;
            } else if (mousePosition.x > this.position.x + this.size.width) {
                this.circlePosition.x = this.position.x + this.size.width;
            } else {
                this.circlePosition.x = mousePosition.x;
            }
            if (mousePosition.y < this.position.y) {
                this.circlePosition.y = this.position.y;
            } else if (mousePosition.y > this.position.y + this.size.height) {
                this.circlePosition.y = this.position.y + this.size.height;
            } else {
                this.circlePosition.y = mousePosition.y;
            }
            this.value = calculateValue();
        } else if (mouseButton.leftIsPressed) {
            this.isPressed = this.circlePosition.distance(new Vector2D(mousePosition.x, mousePosition.y)) < 10;
        }
        if (!mouseButton.leftIsPressed) {
            this.isPressed = false;
        }
    }

    public void render(Graphics2D g2, double scale) {
        g2.setColor(new Color(0, 0, 0, 0.5f));
        g2.fillRect((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.size.width * scale), (int) (this.size.height * scale));
//        g2.setColor(this.frameColor);
//        g2.drawRect((int) (this.position.x * scale), (int) (this.position.y * scale), (int) (this.size.width * scale), (int) (this.size.height * scale));
        g2.setColor(this.circleColor);
        g2.fillOval((int) ((this.circlePosition.x - 10) * scale), (int) ((this.circlePosition.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
    }

}
