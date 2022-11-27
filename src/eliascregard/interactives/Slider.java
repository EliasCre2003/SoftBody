package eliascregard.interactives;

import eliascregard.input.MouseHandler;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Slider {

    public double value;
    public double min;
    public double max;
    public double step;
    public double length;
    public boolean isPressed;
    public boolean isHorizontal;
    public Color circleColor;
    public Color lineColor;
    public Vector2D position;
    public Vector2D circlePosition;

    public Slider(
            double startValue, double min, double max, double step, double length, boolean isHorizontal,
            Color circleColor, Color lineColor, Vector2D position
    ) {
        this.value = startValue;
        this.min = min;
        this.max = max;
        this.step = step;
        this.length = length;
        this.isPressed = false;
        this.isHorizontal = isHorizontal;
        this.circleColor = circleColor;
        this.lineColor = lineColor;
        this.position = position;
        this.circlePosition = calculateCirclePosition();
    }

    public void update(MouseHandler mouse) {
        if (this.isPressed) {
            if (this.isHorizontal) {
                if (mouse.x < this.position.x) {
                    this.circlePosition.x = this.position.x;
                } else if (mouse.x > this.position.x + this.length) {
                    this.circlePosition.x = this.position.x + this.length;
                } else {
                    this.circlePosition.x = mouse.x;
                }
            } else {
                if (mouse.y < this.position.y) {
                    this.circlePosition.y = this.position.y;
                } else if (mouse.x > this.position.y + this.length) {
                    this.circlePosition.y = this.position.y + this.length;
                } else {
                    this.circlePosition.y = mouse.y;
                }
            }
            this.value = calculateValue();
        } else if (mouse.pressed) {
            this.isPressed = this.circlePosition.distance(new Vector2D(mouse.x, mouse.y)) < 10;
        }
        if (!mouse.pressed) {
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

}
