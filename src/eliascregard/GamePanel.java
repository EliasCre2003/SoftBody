package eliascregard;

import eliascregard.input.*;
import eliascregard.physics.*;
import eliascregard.physics.Spring;

import javax.swing.*;
import java.awt.*;
import java.awt.Polygon;


public class GamePanel extends JPanel implements Runnable {
    final Dimension SCREEN_SIZE = Main.SCREEN_SIZE;
    final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1920, 1080);
    final double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    int MAX_FRAME_RATE = 200;
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

    public Vector2D gravity = new Vector2D(0, 500);

    SpringBody[] bodies;
    StaticObject[] staticObjects;

    boolean renderMode = true;

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
        bodies = new SpringBody[1];
        for (int i = 0; i < bodies.length; i++) {
            bodies[i] = SpringBody.homogeneousRectangle(800 + ((i%7) * 250), 4 + (double) (i / 7) * 250,
                    8, 8, 1, 5000, 100, 5, 10);
        }

        staticObjects = new StaticObject[2];
        staticObjects[0] = new StaticObject(
                new Vector2D[] {
                        new Vector2D(100, 100),
                        new Vector2D(200, 100),
                        new Vector2D(200, 200),
                        new Vector2D(100, 200)
                }
        );
        staticObjects[1] = new StaticObject(
                new Vector2D[] {
                        new Vector2D(300, 100),
                        new Vector2D(400, 300),
                        new Vector2D(400, 500),
                        new Vector2D(300, 300)
                }
        );


        while (gameThread != null) {
            deltaT = time.getDeltaTime();
            tickSpeed = time.getFPS(deltaT);
            renderDeltaT += deltaT;
            if (MAX_FRAME_RATE == 0) {
                fps = time.getFPS(deltaT);
            }

            if (keyH.escapePressed) {
                System.exit(0);
            }

            update();
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

        if (keyH.rPressed) {
            keyH.rPressed = false;
            renderMode = !renderMode;
        }

        Vector2D movement = new Vector2D();
        if (keyH.rightPressed) {
            keyH.rightPressed = false;
            movement.x += 1000;
        }
        if (keyH.leftPressed) {
            keyH.leftPressed = false;
            movement.x -= 1000;
        }
        if (keyH.upPressed) {
            keyH.upPressed = false;
            movement.y -= 1000;
        }
        if (keyH.downPressed) {
            keyH.downPressed = false;
            movement.y += 1000;
        }

        for (SpringBody body : bodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                for (StaticObject staticObject : staticObjects) {
                    if (node.insidePerimeter(staticObject)) {
                        node.resolveCollision(staticObject);
                    }
                }

                if (node.position.x < 0) {
                    if (node.velocity.x < 0) {
                        node.velocity.x = -node.velocity.x;
                    }
                    node.position.x = 0;
                } else if (node.position.x > DEFAULT_SCREEN_SIZE.width) {
                    if (node.velocity.x > 0) {
                        node.velocity.x = -node.velocity.x;
                    }
                    node.position.x = DEFAULT_SCREEN_SIZE.width;
                }
                if (node.position.y < 0) {
                    if (node.velocity.y < 0) {
                        node.velocity.y = -node.velocity.y;
                    }
                    node.position.y = 0;
                } else if (node.position.y > DEFAULT_SCREEN_SIZE.height) {
                    if (node.velocity.y > 0) {
                        node.velocity.y = -node.velocity.y;
                    }
                    node.position.y = DEFAULT_SCREEN_SIZE.height;
                }

                for (SpringBody otherBody : bodies) {
                    for (Node otherNode : otherBody.nodes) {
                        if (node.isColliding(otherNode) && node != otherNode) {
                            node.resolveCollision(otherNode);
                        }
                    }
                }
            }
        }

        for (SpringBody body : bodies) {
            body.update(deltaT, gravity);
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        for (SpringBody body : bodies) {
            if (body == null) {
                continue;
            }
            if (renderMode) {
                g2.setColor(new Color(255,255,255));
                g2.setStroke(new BasicStroke((float) (3*SCREEN_SCALE)));
                for (Spring spring : body.springs) {
                    if (spring != null) {
                        g2.drawLine((int)(spring.node1.position.x*SCREEN_SCALE), (int)(spring.node1.position.y*SCREEN_SCALE), (int)(spring.node2.position.x*SCREEN_SCALE), (int)(spring.node2.position.y*SCREEN_SCALE));
                    }
                }
                g2.setColor(new Color(255, 0, 0));
                for (Node node : body.nodes) {
                    g2.fillOval((int)((node.position.x - node.radius)*SCREEN_SCALE), (int)((node.position.y - node.radius)*SCREEN_SCALE), (int) (node.radius * 2 * SCREEN_SCALE), (int) (node.radius * 2 * SCREEN_SCALE));
                }
            }
            else {
                g2.setColor(new Color(0,0,255));
                Polygon polly = new Polygon();
                for (int i = 0; i < body.height - 1; i++) {
                    polly.addPoint((int)((body.nodes[i].position.x)*SCREEN_SCALE), (int)((body.nodes[i].position.y)*SCREEN_SCALE));
                }
                for (int i = 1; i < body.width; i++) {
                    polly.addPoint((int)((body.nodes[i * body.height - 1].position.x)*SCREEN_SCALE), (int)((body.nodes[i * body.height - 1].position.y)*SCREEN_SCALE));
                }
                for (int i = 1; i < body.height; i++) {
                    polly.addPoint((int)((body.nodes[body.nodes.length - i].position.x)*SCREEN_SCALE), (int)((body.nodes[body.nodes.length - i].position.y)*SCREEN_SCALE));
                }
                for (int i = 1; i < body.width; i++) {
                    polly.addPoint((int)((body.nodes[body.nodes.length - i * body.height].position.x)*SCREEN_SCALE), (int)((body.nodes[body.nodes.length - i * body.height].position.y)*SCREEN_SCALE));
                }
                g2.fillPolygon(polly);
            }
        }

        g2.setColor(new Color(255,255,255));
        for (StaticObject staticObject : staticObjects) {
            if (staticObject == null) {
                continue;
            }
            g2.drawPolygon(staticObject.getPolygon(SCREEN_SCALE));
        }

        g2.setColor(new Color(255, 255, 255));
        g2.drawString("FPS: " + fps, 10, 20);
        g2.drawString("Tickspeed: " + tickSpeed, 10, 40);


        g2.dispose();
    }
}