package eliascregard.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean escapePressed = false;
    public boolean spacePressed = false;
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean enterPressed = false;
    public boolean qPressed = false;
    public boolean wPressed = false;
    public boolean ePressed = false;
    public boolean rPressed = false;
    public boolean tPressed = false;
    public boolean yPressed = false;
    public boolean uPressed = false;
    public boolean iPressed = false;
    public boolean oPressed = false;
    public boolean pPressed = false;
    public boolean aPressed = false;
    public boolean sPressed = false;
    public boolean dPressed = false;
    public boolean fPressed = false;
    public boolean gPressed = false;
    public boolean hPressed = false;
    public boolean jPressed = false;
    public boolean kPressed = false;
    public boolean lPressed = false;
    public boolean zPressed = false;
    public boolean xPressed = false;
    public boolean cPressed = false;
    public boolean vPressed = false;
    public boolean bPressed = false;
    public boolean nPressed = false;
    public boolean mPressed = false;
    public boolean onePressed = false;
    public boolean twoPressed = false;
    public boolean threePressed = false;
    public boolean fourPressed = false;
    public boolean fivePressed = false;
    public boolean sixPressed = false;
    public boolean sevenPressed = false;
    public boolean eightPressed = false;
    public boolean ninePressed = false;
    public boolean zeroPressed = false;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = true;
        }
        if (code == KeyEvent.VK_W) {
            wPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }
        if (code == KeyEvent.VK_R) {
            rPressed = true;
        }
        if (code == KeyEvent.VK_T) {
            tPressed = true;
        }
        if (code == KeyEvent.VK_Y) {
            yPressed = true;
        }
        if (code == KeyEvent.VK_U) {
            uPressed = true;
        }
        if (code == KeyEvent.VK_I) {
            iPressed = true;
        }
        if (code == KeyEvent.VK_O) {
            oPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            pPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            aPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            sPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            dPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = true;
        }
        if (code == KeyEvent.VK_G) {
            gPressed = true;
        }
        if (code == KeyEvent.VK_H) {
            hPressed = true;
        }
        if (code == KeyEvent.VK_J) {
            jPressed = true;
        }
        if (code == KeyEvent.VK_K) {
            kPressed = true;
        }
        if (code == KeyEvent.VK_L) {
            lPressed = true;
        }
        if (code == KeyEvent.VK_Z) {
            zPressed = true;
        }
        if (code == KeyEvent.VK_X) {
            xPressed = true;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = true;
        }
        if (code == KeyEvent.VK_V) {
            vPressed = true;
        }
        if (code == KeyEvent.VK_B) {
            bPressed = true;
        }
        if (code == KeyEvent.VK_N) {
            nPressed = true;
        }
        if (code == KeyEvent.VK_M) {
            mPressed = true;
        }
        if (code == KeyEvent.VK_1) {
            onePressed = true;
        }
        if (code == KeyEvent.VK_2) {
            twoPressed = true;
        }
        if (code == KeyEvent.VK_3) {
            threePressed = true;
        }
        if (code == KeyEvent.VK_4) {
            fourPressed = true;
        }
        if (code == KeyEvent.VK_5) {
            fivePressed = true;
        }
        if (code == KeyEvent.VK_6) {
            sixPressed = true;
        }
        if (code == KeyEvent.VK_7) {
            sevenPressed = true;
        }
        if (code == KeyEvent.VK_8) {
            eightPressed = true;
        }
        if (code == KeyEvent.VK_9) {
            ninePressed = true;
        }
        if (code == KeyEvent.VK_0) {
            zeroPressed = true;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = false;
        }
        if (code == KeyEvent.VK_W) {
            wPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (code == KeyEvent.VK_R) {
            rPressed = false;
        }
        if (code == KeyEvent.VK_T) {
            tPressed = false;
        }
        if (code == KeyEvent.VK_Y) {
            yPressed = false;
        }
        if (code == KeyEvent.VK_U) {
            uPressed = false;
        }
        if (code == KeyEvent.VK_I) {
            iPressed = false;
        }
        if (code == KeyEvent.VK_O) {
            oPressed = false;
        }
        if (code == KeyEvent.VK_P) {
            pPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            aPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            sPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            dPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = false;
        }
        if (code == KeyEvent.VK_G) {
            gPressed = false;
        }
        if (code == KeyEvent.VK_H) {
            hPressed = false;
        }
        if (code == KeyEvent.VK_J) {
            jPressed = false;
        }
        if (code == KeyEvent.VK_K) {
            kPressed = false;
        }
        if (code == KeyEvent.VK_L) {
            lPressed = false;
        }
        if (code == KeyEvent.VK_Z) {
            zPressed = false;
        }
        if (code == KeyEvent.VK_X) {
            xPressed = false;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = false;
        }
        if (code == KeyEvent.VK_V) {
            vPressed = false;
        }
        if (code == KeyEvent.VK_B) {
            bPressed = false;
        }
        if (code == KeyEvent.VK_N) {
            nPressed = false;
        }
        if (code == KeyEvent.VK_M) {
            mPressed = false;
        }

    }
}