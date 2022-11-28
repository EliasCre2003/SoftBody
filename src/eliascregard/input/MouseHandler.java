package eliascregard.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {

    public boolean pressed = false;
    public int x, y;

    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        pressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void mouseExited(MouseEvent e) {
        pressed = false;
    }

    public void mouseEntered(MouseEvent e) {
        pressed = false;
    }

    public void mouseClicked(MouseEvent e) {
        pressed = false;
    }

    public void mouseWheelMoved(MouseEvent e) {
        pressed = false;
    }

}
