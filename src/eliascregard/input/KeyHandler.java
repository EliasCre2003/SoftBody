package eliascregard.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    private final boolean[] keys = new boolean[256];
    private final boolean[] justPressedKeys = new boolean[256];

    public boolean isKeyDown(int keyCode) {
        justPressedKeys[keyCode] = false;
        return keys[keyCode];
    }
    public boolean isKeyJustPressed(int keyCode) {
        if (justPressedKeys[keyCode]) {
            justPressedKeys[keyCode] = false;
            return true;
        }
        return false;
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < keys.length) {
            justPressedKeys[code] = true;
            keys[code] = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < keys.length) {
            keys[code] = false;
            justPressedKeys[code] = false;
        }
    }
}