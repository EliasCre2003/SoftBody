package eliascregard.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {

    private int x = 0, y = 0;
    private final double scale;

    private boolean leftIsPressed = false;
    private boolean rightIsPressed = false;

    private int wheelRotation = 0;

    public MouseHandler(double scale) {
        super();
        this.scale = scale;
    }
    public MouseHandler() {
        this(1);
    }

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftIsPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightIsPressed = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftIsPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightIsPressed = false;
        }
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
    }

    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        wheelRotation += e.getWheelRotation();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean leftIsPressed() {
        return leftIsPressed;
    }

    public boolean rightIsPressed() {
        return rightIsPressed;
    }

    public void setLeftIsPressed(boolean leftIsPressed) {
        this.leftIsPressed = leftIsPressed;
    }

    public void setRightIsPressed(boolean rightIsPressed) {
        this.rightIsPressed = rightIsPressed;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }

}
