package eliascregard;

import eliascregard.input.*;
import eliascregard.physics.*;
import eliascregard.physics.Spring;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;


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
    MouseHandler mouseH = new MouseHandler();
    double deltaT;
    public int tickSpeed;
    double renderDeltaT = 0;
    public int fps;

    public Vector2D gravity = new Vector2D(0, 500);

    SpringBody[] springBodies;
    SpringBody defaultSpringBody = SpringBody.homogeneousRectangle(800,  0,
            8, 8, 1, 1000, 100, 5, 10);
    StaticObject[] staticObjects;
    RigidBody[] rigidBodies;
    final StaticObject[] screenBounds = new StaticObject[] {
            new StaticObject(
                    new Vector2D[] {
                            new Vector2D(-1000,-1000),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width, -1000),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width, 0),
                            new Vector2D(-1000, 0)
                    },
                    1, 0.5
            ),
            new StaticObject(
                    new Vector2D[] {
                            new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, -1000),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, DEFAULT_SCREEN_SIZE.height),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width, DEFAULT_SCREEN_SIZE.height),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width, 0)
                    },
                    1, 0.5
            ),
            new StaticObject(
                    new Vector2D[] {
                            new Vector2D(0, DEFAULT_SCREEN_SIZE.height + 1000),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, DEFAULT_SCREEN_SIZE.height + 1000),
                            new Vector2D(DEFAULT_SCREEN_SIZE.width + 1000, DEFAULT_SCREEN_SIZE.height),
                            new Vector2D(0, DEFAULT_SCREEN_SIZE.height)
                    },
                    1, 0
            ),
            new StaticObject(
                    new Vector2D[] {
                            new Vector2D(-1000, 0),
                            new Vector2D(0, 0),
                            new Vector2D(0, DEFAULT_SCREEN_SIZE.height + 1000),
                            new Vector2D(-1000, DEFAULT_SCREEN_SIZE.height + 1000)
                    },
                    1, 0.5
            )
    };

    int renderMode = 0;

    void resetBodies() {
        springBodies = new SpringBody[0];
        rigidBodies = new RigidBody[0];
    }

    void wait(int milliseconds) {
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
        springBodies = new SpringBody[1];
        for (int i = 0; i < springBodies.length; i++) {
            springBodies[i] = SpringBody.homogeneousRectangle(800 + ((i%7) * 250), 4 + (double) (i / 7) * 250,
                    8, 8, 1, 5000, 100, 5, 10);
        }

        staticObjects = new StaticObject[2];
        staticObjects[0] = new StaticObject(
                new Vector2D[] {
                        new Vector2D(100, 100),
                        new Vector2D(200, 100),
                        new Vector2D(200, 200),
                        new Vector2D(100, 200)
                },
                0, 0.5
        );
        staticObjects[1] = new StaticObject(
                new Vector2D[] {
                        new Vector2D(300, 100),
                        new Vector2D(400, 300),
                        new Vector2D(400, 500),
                        new Vector2D(300, 300)
                },
                0,0.5
        );
        rigidBodies = new RigidBody[1];
        rigidBodies[0] = new RigidBody(
                new Vector2D(
                        500,
                        100
                ),
                100, 100, 1, 0
        );



        while (gameThread != null) {
            deltaT = time.getDeltaTime();
            if (ticks == 0) {deltaT = 0;}
            tickSpeed = time.getFPS(deltaT);
            renderDeltaT += deltaT;
            if (MAX_FRAME_RATE == 0) {
                fps = time.getFPS(deltaT);
            }

            if (keyH.escapePressed) {
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
//        System.out.println(mouseH.x + ", " + mouseH.y);

        if (keyH.enterPressed) {
            keyH.enterPressed = false;
            springBodies = Arrays.copyOf(springBodies, springBodies.length + 1);
            springBodies[springBodies.length - 1] = defaultSpringBody.makeCopy();
        }
        if (keyH.rPressed) {
            keyH.rPressed = false;
            resetBodies();
        }
        if (keyH.tPressed) {
            keyH.tPressed = false;
            renderMode = (renderMode + 1) % 3;
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

        for (SpringBody body : springBodies) {
            for (Node node : body.nodes) {

                node.velocity.add(movement);

                for (StaticObject staticObject : staticObjects) {
                    if (node.insidePerimeter(staticObject)) {
                        node.resolveCollision(staticObject);
                    }
                }

//                if (node.position.x < 0) {
//                    if (node.velocity.x < 0) {
//                        node.velocity.x = -node.velocity.x;
//                    }
//                    node.position.x = 0;
//                } else if (node.position.x > DEFAULT_SCREEN_SIZE.width) {
//                    if (node.velocity.x > 0) {
//                        node.velocity.x = -node.velocity.x;
//                    }
//                    node.position.x = DEFAULT_SCREEN_SIZE.width;
//                }
//                if (node.position.y < 0) {
//                    if (node.velocity.y < 0) {
//                        node.velocity.y = -node.velocity.y;
//                    }
//                    node.position.y = 0;
//                } else if (node.position.y > DEFAULT_SCREEN_SIZE.height) {
//                    if (node.velocity.y > 0) {
//                        node.velocity.y = -node.velocity.y;
//                    }
//                    node.position.y = DEFAULT_SCREEN_SIZE.height;
//                }

                for (StaticObject bound : screenBounds) {
                    if (node.insidePerimeter(bound)) {
                        node.resolveCollision(bound);
                    }
                }

                for (SpringBody otherBody : springBodies) {
                    for (Node otherNode : otherBody.nodes) {
                        if (node.isColliding(otherNode) && node != otherNode) {
                            node.resolveCollision(otherNode);
                        }
                    }
                }

            }
        }

        for (SpringBody body : springBodies) {
            body.update(deltaT, gravity);
        }
        for (RigidBody rigidBody : rigidBodies) {
            if (deltaT > 0) {
                rigidBody.applyForce(movement.scaled(1/deltaT), new Vector2D(50, 50));
            }
            rigidBody.update(deltaT, gravity);
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        int totalNodes = 0;
        int totalSprings = 0;
        for (SpringBody body : springBodies) {
            if (body == null) {
                continue;
            }
            totalNodes += body.nodes.length;
            totalSprings += body.springs.length;
            if (renderMode == 0 || renderMode == 2) {
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
            if (renderMode == 1) {
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
            if (renderMode == 2) {
                g2.setColor(new Color(0,255,0));
                for (Node node : body.nodes) {
                    Point p1 = new Point((int)(node.position.x*SCREEN_SCALE), (int)(node.position.y*SCREEN_SCALE));
                    Point p2 = new Point((int)((node.position.x + node.velocity.x * 0.1)*SCREEN_SCALE), (int)((node.position.y + node.velocity.y * 0.1)*SCREEN_SCALE));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        g2.setColor(new Color(255,255,255));
        g2.setStroke(new BasicStroke((float) (2*SCREEN_SCALE)));
        for (StaticObject staticObject : staticObjects) {
            if (staticObject == null) {
                continue;
            }
            g2.drawPolygon(staticObject.getPolygon(SCREEN_SCALE));
        }

        g2.setColor(new Color(0, 255, 0));
        for (RigidBody rigidBody : rigidBodies) {
            if (rigidBody == null) {
                System.out.println("null");
                continue;
            }
            g2.drawPolygon(rigidBody.getPolygon(SCREEN_SCALE));
        }

        if (renderMode == 2) {
            g2.setColor(new Color(255, 255, 255));
            g2.drawString("FPS: " + fps, 10, 20);
            g2.drawString("Tickspeed: " + tickSpeed, 10, 40);
            g2.drawString("Gravity: " + gravity, 10, 60);
            g2.drawString("Total Nodes: " + totalNodes, 10, 80);
            g2.drawString("Total Springs: " + totalSprings, 10, 100);
        }

        g2.dispose();
    }
}