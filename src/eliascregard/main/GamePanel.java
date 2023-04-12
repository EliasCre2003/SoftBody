package eliascregard.main;

import eliascregard.input.*;
import eliascregard.interactives.*;
import eliascregard.interactives.ButtonGroup;
import eliascregard.math.vectors.Vector2;
import eliascregard.physics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class GamePanel extends JPanel implements Runnable {
    private Dimension SCREEN_SIZE = Main.SCREEN_SIZE;
    private final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1920, 1080);
    private double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    private int MAX_FRAME_RATE = 200;
    private Thread gameThread;
    int ticks = 0;
    private final GameTime time = new GameTime();
    private final KeyHandler keys = new KeyHandler();
    private final MouseHandler mouse = new MouseHandler(SCREEN_SCALE);
    private double deltaTime;
    private int tickSpeed;
    private double renderDeltaT = 0;
    private int fps;

    private int renderMode = 0;

    private final Slider2D gravitySlider = new Slider2D(
            new Vector2(0, 500), -1000, 1000, -1000, 1000,
            new Dimension(200, 200), new Color(255, 0, 0), new Color(255,255,255),
            new Vector2(DEFAULT_SCREEN_SIZE.width - 15 - 200, 15)
    );
    private final PhysicsSpace mainSpace = new PhysicsSpace(gravitySlider.value);

    private final Slider timeMultiplierSlider = new Slider(
            "Time", 1, 0, 2, 200, true,
            new Color(255,0,0), new Color(255,255,255),
            new Vector2(DEFAULT_SCREEN_SIZE.width - 15 - 200, 230)
    );
    private double timeMultiplier = timeMultiplierSlider.value;

    private final Slider frictionSlider = new Slider(
            "Friction", 0.1, 0, 1, 200, true,
            new Color(255,0,0), new Color(255,255,255),
            new Vector2(DEFAULT_SCREEN_SIZE.width - 15 - 200, 270)
    );
    private double friction = frictionSlider.value;

    private final CircularButton zeroGravityButton = new CircularButton(
            "Zero Gravity", new Vector2(DEFAULT_SCREEN_SIZE.width - 15 - 200, 300),
            new Color(255, 255, 255), new Color(255, 0, 0), 10
    );

    private final ButtonGroup rightClickMenu = new ButtonGroup(new Vector2(0,0));

    private void sleep(int nanoseconds) {
        try {

            Thread.sleep((long) GameTime.nanoSecondsToMilliSeconds(nanoseconds), nanoseconds % 1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(new Color(0, 0, 0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keys);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.setFocusable(true);
    }


    @Override
    public void run() {

        mainSpace.setGravity(gravitySlider.value);
        mainSpace.setBoundary(new Boundary(new Vector2(0, 0), DEFAULT_SCREEN_SIZE.width, DEFAULT_SCREEN_SIZE.height, 100000, 0, 1));

        mainSpace.addStaticObject(new StaticObject(
                new Vector2[] {
                        new Vector2(100, 100),
                        new Vector2(200, 100),
                        new Vector2(200, 200),
                        new Vector2(100, 200)
                },
                1, 1
        ));
        mainSpace.addStaticObject(new StaticObject(
                new Vector2[] {
                        new Vector2(300, 100),
                        new Vector2(400, 300),
                        new Vector2(400, 500),
                        new Vector2(300, 300)
                },
                1,1
        ));

        rightClickMenu.setVisibility(false);
        rightClickMenu.addButton(new RectangularButton("Reset", new Vector2(0,0), new Color(25,25,25),
                new Dimension(150, 35), true));

        while (gameThread != null) {
            deltaTime = time.getDeltaTime();
            if (ticks == 0) {
                deltaTime = 0;
            }
            tickSpeed = time.getFPS(deltaTime);
            renderDeltaT += deltaTime;
            deltaTime *= timeMultiplier;
            if (MAX_FRAME_RATE == 0) {
                fps = time.getFPS(deltaTime);
            }

            if (keys.isKeyDown(KeyEvent.VK_ESCAPE)) {
                System.exit(0);
            }
            update();
            ticks++;
            if (MAX_FRAME_RATE > 0) {
                if (renderDeltaT >= 1.0 / MAX_FRAME_RATE) {
                    fps = time.getFPS(renderDeltaT);
                    repaint();
                    renderDeltaT -= 1.0 / MAX_FRAME_RATE;
                }
            } else {
                repaint();
            }
        }
    }

    public void update() {

        if (keys.isKeyJustPressed(KeyEvent.VK_T)) {
            renderMode = (renderMode + 1) % 2;
        }

        mainSpace.update(deltaTime, keys, mouse);

        gravitySlider.update(mouse);
        mainSpace.setGravity(gravitySlider.value);
        timeMultiplierSlider.update(mouse);
        timeMultiplier = timeMultiplierSlider.value;
        frictionSlider.update(mouse);
        mainSpace.setBoundaryFriction(frictionSlider.value);
        zeroGravityButton.update(mouse);
        if (zeroGravityButton.getState()) {
            Vector2 newGravity = new Vector2(0, 0);
            gravitySlider.setValue(newGravity);
            mainSpace.setGravity(newGravity);
        }
        if (mouse.leftIsPressed()) {
            rightClickMenu.setVisibility(false);
        }
        if (mouse.rightIsPressed()) {
            mouse.setRightIsPressed(false);
            rightClickMenu.setVisibility(true);
            rightClickMenu.setPosition(mouse.getX(), mouse.getY());
        }
        rightClickMenu.update(mouse);
        if (rightClickMenu.getButton(0).getState()) {
            mainSpace.resetSpringBodies();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        mainSpace.render(g2, SCREEN_SCALE, renderMode);

        gravitySlider.render(g2, SCREEN_SCALE);
        timeMultiplierSlider.render(g2, SCREEN_SCALE);
        frictionSlider.render(g2, SCREEN_SCALE);
        zeroGravityButton.render(g2, SCREEN_SCALE);
        rightClickMenu.render(g2, SCREEN_SCALE);

        if (renderMode == 1) {
            g2.setColor(new Color(0,0,0,0.5f));
            g2.fillRect(0,0, 200, 120);
            g2.setColor(new Color(255, 255, 255));
            g2.drawString("FPS: " + fps, 10, 20);
            g2.drawString("Tickspeed: " + tickSpeed, 10, 40);
            g2.drawString("Gravity: " + mainSpace.getGravity(), 10, 60);
            g2.drawString("Total Nodes: " + mainSpace.getTotalNodes(), 10, 80);
            g2.drawString("Total Springs: " + mainSpace.getTotalSprings(), 10, 100);
        }
        g2.dispose();
    }
}