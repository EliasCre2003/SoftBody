package eliascregard;

import javax.swing.JFrame;
import java.awt.*;

public class Main {
    public static GamePanel gamePanel;
    public static JFrame window;
    public static Dimension SCREEN_SIZE;
    public static void main(String[] args) {
        try {
            SCREEN_SIZE = new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (Exception e) {
            SCREEN_SIZE = new Dimension(1600, 900);
        }

        window = new JFrame("Soft Body");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
//        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(false);

        gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocation(0, 0);
        window.setVisible(true);
        gamePanel.startGameThread();
        System.out.println("Game started");
    }
}