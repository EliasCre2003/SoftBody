package eliascregard;

import eliascregard.input.*;
import eliascregard.interactives.Slider;
import eliascregard.interactives.Slider2D;
import eliascregard.interactives.Switch;
import eliascregard.interactives.Button;
import eliascregard.physics.*;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    final Dimension SCREEN_SIZE = Main.SCREEN_SIZE;
    final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1920, 1080);
    final double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    int MAX_FRAME_RATE = 200;
    int MAX_TICKSPEED = 0;
    public Thread gameThread;
    int ticks = 0;
    GameTime time = new GameTime();
    KeyHandler keyH = new KeyHandler();
    MouseButtonHandler mouse = new MouseButtonHandler();
    MouseMovementHandler mouseMovement = new MouseMovementHandler(SCREEN_SCALE);
    double deltaTime;
    int tickSpeed;
    double renderDeltaT = 0;
    int fps;

    PhysicsSpace mainSpace = new PhysicsSpace();

    int renderMode = 2;

    Slider2D gravitySlider = new Slider2D(
            new Vector2D(0, 500), -1000, 1000, -1000, 1000,
            new Dimension(200, 200), new Color(255, 0, 0), new Color(255,255,255),
            new Vector2D(DEFAULT_SCREEN_SIZE.width - 15 - 200, 15)
    );

    Slider timeMultiplierSlider = new Slider(
            1, 0, 2, 200, true,
            new Color(255,0,0), new Color(255,255,255),
            new Vector2D(DEFAULT_SCREEN_SIZE.width - 15 - 200, 230)
    );
    double timeMultiplier = timeMultiplierSlider.value;

    Switch tickDelaySwitch = new Switch(
            new Color(255, 255, 255), new Color(0, 255, 0), "Tick Delay",
            new Vector2D(DEFAULT_SCREEN_SIZE.width - 15 - 200, 260)
    );

    Button zeroGravityButton = new Button(
            new Color(255, 255, 255), new Color(255, 0, 0), "Zero Gravity",
            new Vector2D(DEFAULT_SCREEN_SIZE.width - 15 - 200, 290)
    );

    void sleep(int nanoseconds) {
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
        this.addKeyListener(keyH);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouseMovement);
        this.setFocusable(true);
    }


    @Override
    public void run() {

        mainSpace.setGravity(gravitySlider.value);
        mainSpace.setBoundary(new StaticObject(
                new Vector2D[] {
                        new Vector2D((double) DEFAULT_SCREEN_SIZE.width / 2, -1000),
                        new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, -1000),
                        new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, DEFAULT_SCREEN_SIZE.height + 1000),
                        new Vector2D(-1000, DEFAULT_SCREEN_SIZE.height + 1000),
                        new Vector2D(-1000, -1000),
                        new Vector2D((double) DEFAULT_SCREEN_SIZE.width / 2, -1000),
                        new Vector2D((double) DEFAULT_SCREEN_SIZE.width / 2, 0),
                        new Vector2D(0, 0),
                        new Vector2D(0, DEFAULT_SCREEN_SIZE.height),
                        new Vector2D(DEFAULT_SCREEN_SIZE.width, DEFAULT_SCREEN_SIZE.height),
                        new Vector2D(DEFAULT_SCREEN_SIZE.width, 0),
                        new Vector2D((double) DEFAULT_SCREEN_SIZE.width / 2, 0)
                }, 1, 1
        ));

        mainSpace.addStaticObject(new StaticObject(
                new Vector2D[] {
                        new Vector2D(100, 100),
                        new Vector2D(200, 100),
                        new Vector2D(200, 200),
                        new Vector2D(100, 200)
                },
                0, 1
        ));
        mainSpace.addStaticObject(new StaticObject(
                new Vector2D[] {
                        new Vector2D(300, 100),
                        new Vector2D(400, 300),
                        new Vector2D(400, 500),
                        new Vector2D(300, 300)
                },
                0,1
        ));

        double lastDelay = 0;
        while (gameThread != null) {
            deltaTime = time.getDeltaTime() - GameTime.nanoSecondsToSeconds(lastDelay);
            if (ticks == 0) {
                deltaTime = 0;}
            tickSpeed = time.getFPS(deltaTime);
            renderDeltaT += deltaTime;
            deltaTime *= timeMultiplier;
            if (MAX_FRAME_RATE == 0) {
                fps = time.getFPS(deltaTime);
            }

            if (keyH.escapePressed) {
                System.exit(0);
            }
            if (tickDelaySwitch.isOn) {
                sleep(1_000_000_000);
                lastDelay = 1_000_000_000;
            }
            else {
                lastDelay = 0;
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

        if (keyH.tPressed) {
            keyH.tPressed = false;
            renderMode = (renderMode + 1) % 3;
        }

        mainSpace.update(deltaTime, keyH, mouse, mouseMovement);

        gravitySlider.update(mouse, mouseMovement);
        mainSpace.setGravity(gravitySlider.value);
        timeMultiplierSlider.update(mouse, mouseMovement);
        timeMultiplier = timeMultiplierSlider.value;
        tickDelaySwitch.update(mouse, mouseMovement);
        zeroGravityButton.update(mouse, mouseMovement);
        if (zeroGravityButton.isPressed) {
            System.out.println("Zero Gravity");
            Vector2D newGravity = new Vector2D(0, 0);
            gravitySlider.setValue(newGravity);
            mainSpace.setGravity(newGravity);
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
        tickDelaySwitch.render(g2, SCREEN_SCALE);
        zeroGravityButton.render(g2, SCREEN_SCALE);

        if (renderMode == 2) {
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