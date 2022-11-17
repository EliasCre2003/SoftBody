package eliascregard;

import eliascregard.input.*;
import eliascregard.physics.*;
import eliascregard.physics.Spring;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    final Dimension SCREEN_SIZE = new Dimension(1600, 900);
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

    public Vector2D gravity = new Vector2D(0, 500);


    SpringBody[] bodies = new SpringBody[4];

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

        for (int i = 0; i < bodies.length; i++) {
            bodies[i] = SpringBody.makeRectangle(600, 100 + i * 250, 8, 8, 1, 2000, 300);
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

                node.applyForce(movement);

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
            for (Node node : body.nodes) {
                node.update(deltaT, gravity);
            }
            int i = 0;
            for (Spring spring : body.springs) {
                spring.update(deltaT);
                i++;
            }
        }


//        for (Node node : test.nodes) {
//            node.applyForce(movement);
//        }
//        test.update(deltaT);
//
//        for (Node[] body : bodies) {
//            for (Node node : body) {
//
//                node.applyForce(movement);
//
//                if (node.position.x - Node.NODE_RADIUS < 0) {
//                    if (node.velocity.x < 0) {
//                        node.velocity.x = -node.velocity.x;
//                    }
//                    node.position.x = Node.NODE_RADIUS;
//                } else if (node.position.x + Node.NODE_RADIUS > DEFAULT_SCREEN_SIZE.width) {
//                    if (node.velocity.x > 0) {
//                        node.velocity.x = -node.velocity.x;
//                    }
//                    node.position.x = DEFAULT_SCREEN_SIZE.width - Node.NODE_RADIUS;
//                }
//                if (node.position.y - Node.NODE_RADIUS < 0) {
//                    if (node.velocity.y < 0) {
//                        node.velocity.y = -node.velocity.y;
//                    }
//                    node.position.y = Node.NODE_RADIUS;
//                } else if (node.position.y + Node.NODE_RADIUS > DEFAULT_SCREEN_SIZE.height) {
//                    if (node.velocity.y > 0) {
//                        node.velocity.y = -node.velocity.y;
//                    }
//                    node.position.y = DEFAULT_SCREEN_SIZE.height - Node.NODE_RADIUS;
//                }
//
//                for (Node[] otherBody : bodies) {
//                    for (Node otherNode : otherBody) {
//                        if (node.isColliding(otherNode) && node != otherNode) {
//                            node.resolveCollision(otherNode);
//                        }
//                    }
//                }
//
//            }
//
//            for (Node node : body) {
//                node.update(deltaT, gravity);
//            }
//        }
//        for (Spring spring : springs) {
//            if (spring == null) {
//                continue;
//            }
//            spring.update(deltaT);
//        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        for (SpringBody body : bodies) {
            g2.setColor(new Color(255,255,255));
            g2.setStroke(new BasicStroke((float) (3*SCREEN_SCALE)));
            for (Spring spring : body.springs) {
                if (spring != null) {
                    g2.drawLine((int)(spring.node1.position.x*SCREEN_SCALE), (int)(spring.node1.position.y*SCREEN_SCALE), (int)(spring.node2.position.x*SCREEN_SCALE), (int)(spring.node2.position.y*SCREEN_SCALE));
                }
            }
            g2.setColor(new Color(255, 0, 0));
            for (Node node : body.nodes) {
                g2.fillOval((int)((node.position.x - Node.NODE_RADIUS)*SCREEN_SCALE), (int)((node.position.y - Node.NODE_RADIUS)*SCREEN_SCALE), (int) (Node.NODE_RADIUS * 2 * SCREEN_SCALE), (int) (Node.NODE_RADIUS * 2 * SCREEN_SCALE));
            }
        }

        g2.setColor(new Color(255, 255, 255));
        g2.drawString("FPS: " + tickSpeed, 10, 20);


        g2.dispose();
    }
}