package eliascregard.physics;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class SpringBody {

    public Node[] nodes;
    public Spring[] springs;
    public int width;
    public int height;

    public SpringBody(Node[] nodes, Spring[] springs, int width, int height) {
        this.nodes = nodes;
        this.springs = springs;
        this.width = width;
        this.height = height;
    }

    public void update(double deltaTime, Vector2D gravity) {
        for (Node node : this.nodes) {
            node.update(deltaTime, gravity);
        }
        for (Spring spring : this.springs) {
            spring.update();
//            if (spring.getLength() > 4 * spring.restLength) {
//                this.removeSpring(spring);
//            }
        }
    }

    public void removeSpring(Spring spring) {
        int index = Arrays.asList(this.springs).indexOf(spring);
        if (this.springs.length - index - 1 >= 0)
            System.arraycopy(this.springs, index + 1, this.springs, index, this.springs.length - index - 1);
    }

    public static SpringBody homogeneousRectangle(double x, double y, int width, int height,
                                                  double nodeMass, double springStiffness, double springDampingFactor,
                                                  double spacing, double nodeRadius) {
        Node[] nodes = new Node[width * height];
        Spring[] springs = new Spring[(width - 1) * height + (height-1) * width + (height - 1) * (width - 1) * 2];

        int nodeIndex = 0;
        int springIndex = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nodes[nodeIndex] = new Node(new Vector2D(x + i * (2*nodeRadius + spacing), y+nodeRadius + j * (2*nodeRadius + spacing)), nodeMass, nodeRadius);
                nodeIndex++;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width-1; j++) {
                springs[springIndex] = new Spring(nodes[i + j * height], nodes[i + (j + 1) * height], springStiffness, springDampingFactor);
                springIndex++;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height-1; j++) {
                springs[springIndex] = new Spring(nodes[i * height + j], nodes[i * height + j + 1], springStiffness, springDampingFactor);
                springIndex++;
            }
        }
        springStiffness = Math.sqrt(2 * Math.pow(springStiffness, 2));
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                springs[springIndex] = new Spring(nodes[i * height + j], nodes[(i+1) * height + j + 1], springStiffness, springDampingFactor);
                springIndex++;
                springs[springIndex] = new Spring(nodes[i * height + j + 1], nodes[(i+1) * height + j], springStiffness, springDampingFactor);
                springIndex++;
            }
        }

        return new SpringBody(nodes, springs, width, height);
    }

    public static SpringBody homogeneousCircle(double x, double y, double radius, int segments, double nodeMass,
                                               double springStiffness, double springDampingFactor, double nodeRadius) {
        Node[] nodes = new Node[segments + 1];
        Spring[] springs = new Spring[segments * 2];

        nodes[0] = new Node(new Vector2D(x, y), nodeMass, nodeRadius);
        double theta = 0;
        for (int i = 0; i < segments; i++) {
            nodes[i+1] = new Node(
                    new Vector2D(x + radius * Math.cos(theta), y + radius * Math.sin(theta)), nodeMass, nodeRadius
            );
            theta += 2 * Math.PI / segments;
        }
        for (int i = 0; i < segments; i++) {
            springs[i] = new Spring(nodes[0], nodes[i+1], springStiffness, springDampingFactor);
            springs[i+segments] = new Spring(nodes[i+1], nodes[(i+1) % segments + 1], 10000, springDampingFactor);
        }

        return new SpringBody(nodes, springs, 1, 1);
    }

    public SpringBody makeCopy() {
        Node[] nodes = new Node[this.nodes.length];
        for (int i = 0; i < this.nodes.length; i++) {
            nodes[i] = this.nodes[i].makeCopy();
        }
        Spring[] springs = new Spring[this.springs.length];
        for (int i = 0; i < this.springs.length; i++) {
            int node1Index = Arrays.asList(this.nodes).indexOf(this.springs[i].node1);
            int node2Index = Arrays.asList(this.nodes).indexOf(this.springs[i].node2);
            springs[i] = new Spring(nodes[node1Index], nodes[node2Index], this.springs[i].stiffness, this.springs[i].dampingFactor);
        }
        return new SpringBody(nodes, springs, this.width, this.height);
    }

    public void render(Graphics2D g2, double scale, int renderingMode) {
        if (renderingMode == 0 || renderingMode == 2) {
            for (Spring spring : this.springs) {
                spring.render(g2, scale);
            }
            for (Node node : this.nodes) {
                node.render(g2, scale);
            }
        }
        if (renderingMode == 2) {
            g2.setColor(new Color(0,255,0));
            for (Node node : this.nodes) {
                Point p1 = new Point((int)(node.position.x*scale), (int)(node.position.y*scale));
                Point p2 = new Point((int)((node.position.x + node.velocity.x * 0.1)*scale), (int)((node.position.y + node.velocity.y * 0.1)*scale));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        else if (renderingMode == 1) {
            g2.setColor(new Color(0,0,255));
            Polygon polly = new Polygon();
            for (int i = 0; i < this.height - 1; i++) {
                polly.addPoint((int)((this.nodes[i].position.x)*scale), (int)((this.nodes[i].position.y)*scale));
            }
            for (int i = 1; i < this.width; i++) {
                polly.addPoint((int)((this.nodes[i * this.height - 1].position.x)*scale), (int)((this.nodes[i * this.height - 1].position.y)*scale));
            }
            for (int i = 1; i < this.height; i++) {
                polly.addPoint((int)((this.nodes[this.nodes.length - i].position.x)*scale), (int)((this.nodes[this.nodes.length - i].position.y)*scale));
            }
            for (int i = 1; i < this.width; i++) {
                polly.addPoint((int)((this.nodes[this.nodes.length - i * this.height].position.x)*scale), (int)((this.nodes[this.nodes.length - i * this.height].position.y)*scale));
            }
            g2.fillPolygon(polly);
        }
    }

}
