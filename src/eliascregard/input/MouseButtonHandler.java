package eliascregard.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseButtonHandler extends MouseAdapter {

    public boolean pressed = false;

    public void mousePressed(MouseEvent e) {
        pressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

}
