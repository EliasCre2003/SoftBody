package eliascregard;

import eliascregard.input.*;
import eliascregard.physics.*;
import eliascregard.physics.Spring;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    final Dimension SCREEN_SIZE = new Dimension(2080, 1170);
    final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1920, 1080);
    final double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    int MAX_FRAME_RATE = 0;
    int MAX_TICKSPEED = 0;
    public Thread gameThread;
    GameTime time = new GameTime();
    KeyHandler keyH = new KeyHandler();
    MouseHandler mouseH = new MouseHandler();
    double deltaT;
    public int tickSpeed;
    double renderDeltaT = 0;
    public int fps;

    final Line[] SCREEN_LINES = new Line[] {
            new Line(new Vector2D(0, 0), new Vector2D(DEFAULT_SCREEN_SIZE.width, 0)),
            new Line(new Vector2D(DEFAULT_SCREEN_SIZE.width, 0), new Vector2D(DEFAULT_SCREEN_SIZE.width, DEFAULT_SCREEN_SIZE.height)),
            new Line(new Vector2D(DEFAULT_SCREEN_SIZE.width, DEFAULT_SCREEN_SIZE.height), new Vector2D(0, DEFAULT_SCREEN_SIZE.height)),
            new Line(new Vector2D(0, DEFAULT_SCREEN_SIZE.height), new Vector2D(0, 0))
    };

    Line someLine = new Line(new Vector2D(700, 1080), new Vector2D(1000, 1000));

    public Vector2D gravity = new Vector2D(0, 500);


    Node[][] bodies = new Node[4][8 * 8];

    Spring[] springs = new Spring[(7 * 8 * 2 + 7 * 7 * 2) * 4];

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
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
        this.addMouseListener(mouseH);
        this.setFocusable(true);
    }

    @Override
    public void run() {
        int springIndex = 0;
        int a = 0;
        for (Node[] body : bodies) {
            int nodeIndex = 0;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    body[nodeIndex] = new Node(new Vector2D(600 + x * 27, Node.NODE_RADIUS + 250*a +y * 27), 5);
                    nodeIndex++;
                }
            }

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 7; y++) {
                    springs[springIndex] = new Spring(body[x + y * 8], body[x + (y + 1) * 8], 10000, 300);
                    springIndex++;
                }
            }
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 8; y++) {
                    springs[springIndex] = new Spring(body[x + y * 8], body[x + 1 + y * 8], 10000, 300);
                    springIndex++;
                }
            }
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 7; y++) {
                    springs[springIndex] = new Spring(body[x + y * 8], body[x + 1 + (y + 1) * 8], 10000, 300);
                    springIndex++;
                    springs[springIndex] = new Spring(body[x + 1 + y * 8], body[x + (y + 1) * 8], 10000, 300);
                    springIndex++;
                }
            }
            a++;
        }

        while (gameThread != null) {
            deltaT = time.getDeltaTime();
            tickSpeed = time.getFPS(deltaT);
            renderDeltaT += deltaT;
            fps = tickSpeed;
            if (fps > MAX_FRAME_RATE && MAX_FRAME_RATE > 0) {
                fps = MAX_FRAME_RATE;
            }

            if (keyH.escapePressed) {
                System.exit(0);
            }

            update();
            if (MAX_FRAME_RATE > 0) {
                if (renderDeltaT >= 1.0 / MAX_FRAME_RATE) {
                    repaint();
                    renderDeltaT -= 1.0 / MAX_FRAME_RATE;
                }
            } else {
                repaint();
            }
        }
    }

    public void update() {
        for (Node[] body : bodies) {
            for (Node node : body) {

                if (keyH.rightPressed) {
                    node.applyForce(new Vector2D(1000, 0), 1);
                }
                if (keyH.leftPressed) {
                    node.applyForce(new Vector2D(-1000, 0), 1);
                }
                if (keyH.upPressed) {
                    node.applyForce(new Vector2D(0, -1000), 1);
                }
                if (keyH.downPressed) {
                    node.applyForce(new Vector2D(0, 1000), 1);
                }

                if (node.position.x - Node.NODE_RADIUS < 0) {
                    if (node.velocity.x < 0) {
                        node.velocity.x = -node.velocity.x;
                    }
                    node.position.x = Node.NODE_RADIUS;
                } else if (node.position.x + Node.NODE_RADIUS > DEFAULT_SCREEN_SIZE.width) {
                    if (node.velocity.x > 0) {
                        node.velocity.x = -node.velocity.x;
                    }
                    node.position.x = DEFAULT_SCREEN_SIZE.width - Node.NODE_RADIUS;
                }
                if (node.position.y - Node.NODE_RADIUS < 0) {
                    if (node.velocity.y < 0) {
                        node.velocity.y = -node.velocity.y;
                    }
                    node.position.y = Node.NODE_RADIUS;
                } else if (node.position.y + Node.NODE_RADIUS > DEFAULT_SCREEN_SIZE.height) {
                    if (node.velocity.y > 0) {
                        node.velocity.y = -node.velocity.y;
                    }
                    node.position.y = DEFAULT_SCREEN_SIZE.height - Node.NODE_RADIUS;
                }

                for (Node[] otherBody : bodies) {
                    for (Node otherNode : otherBody) {
                        if (node.isColliding(otherNode) && node != otherNode) {
                            node.resolveCollision(otherNode);
                        }
                    }
                }

            }

            for (Node node : body) {
                node.update(deltaT, gravity);
            }
        }
        if (keyH.rightPressed) {
            keyH.rightPressed = false;
        }
        if (keyH.leftPressed) {
            keyH.leftPressed = false;
        }
        if (keyH.upPressed) {
            keyH.upPressed = false;
        }
        if (keyH.downPressed) {
            keyH.downPressed = false;
        }
        for (Spring spring : springs) {
            if (spring == null) {
                continue;
            }
            spring.update(deltaT);
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        g2.setColor(new Color(255,255,255));
        g2.drawLine((int) (someLine.point1.x * SCREEN_SCALE), (int)(someLine.point1.y * SCREEN_SCALE), (int) (someLine.point2.x * SCREEN_SCALE), (int) (someLine.point2.y * SCREEN_SCALE));
        for (Spring spring : springs) {
            if (spring == null) {
                continue;
            }
            g2.setStroke(new BasicStroke((int) (3*SCREEN_SCALE)));
            g2.drawLine( (int) (spring.node1.position.x * SCREEN_SCALE), (int) (spring.node1.position.y * SCREEN_SCALE),
                    (int) (spring.node2.position.x * SCREEN_SCALE), (int) (spring.node2.position.y * SCREEN_SCALE));
        }

        g2.setColor(new Color(255, 0, 0));
        for (Node[] body : bodies) {
            for (Node node : body) {
                g2.fillOval((int) (node.position.x * SCREEN_SCALE - Node.NODE_RADIUS * SCREEN_SCALE), (int) (node.position.y * SCREEN_SCALE - Node.NODE_RADIUS * SCREEN_SCALE),
                        (int) (Node.NODE_RADIUS * 2 * SCREEN_SCALE), (int) (Node.NODE_RADIUS * 2 * SCREEN_SCALE));
            }
        }
//

        g2.setColor(new Color(255, 255, 255));
        g2.drawString("FPS: " + tickSpeed, 10, 20);


        g2.dispose();
    }
}