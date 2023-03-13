package eliascregard.physics;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class SpringBody {

    public Node[] nodes;
    public Spring[] springs;

    public SpringBody(Node[] nodes, Spring[] springs) {
        this.nodes = nodes;
        this.springs = springs;
    }
    public void update(double deltaTime, Vector2D gravity) {
        for (Node node : nodes) {
            node.update(deltaTime, gravity);
        }
        for (Spring spring : springs) {
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

        return new SpringBody(nodes, springs);
    }

    public static SpringBody homogeneousTriangle(double x, double y, int size, double nodeMass,
                                                double springStiffness, double springDampingFactor,
                                                double spacing, double nodeRadius) {
        Node[] nodes = new Node[size * (size + 1) / 2];
        Spring[] springs = new Spring[3 * (size * (size - 1)) / 2];
        double actualSpacing = spacing + 2 * nodeRadius;
        int nodeIndex = 0;
        double verticalStep = actualSpacing * Math.sin(Math.PI / 3);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i + 1; j++) {
                Vector2D position = new Vector2D(x - i * actualSpacing / 2 + j * actualSpacing, y + i * verticalStep);
                nodes[nodeIndex] = new Node(position, nodeMass, nodeRadius);
                nodeIndex++;
            }
        }
        int springIndex = 0, currentNode = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < i + 1; j++) {
                springs[springIndex] = new Spring(nodes[currentNode], nodes[currentNode + i + 1], springStiffness, springDampingFactor);
                springs[springIndex + 1] = new Spring(nodes[currentNode], nodes[currentNode + i + 2], springStiffness, springDampingFactor);
                springs[springIndex + 2] = new Spring(nodes[currentNode + i + 1], nodes[currentNode + i + 2], springStiffness, springDampingFactor);
                springIndex += 3;
                currentNode++;
            }
        }
        return new SpringBody(nodes, springs);


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

        return new SpringBody(nodes, springs);
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
        return new SpringBody(nodes, springs);
    }

    public void render(Graphics2D g2, double scale, int renderingMode) {
        for (Spring spring : this.springs) {
            spring.render(g2, scale);
        }
        for (Node node : this.nodes) {
            node.render(g2, scale);
        }
        if (renderingMode == 1) {
            g2.setColor(new Color(0,255,0));
            for (Node node : this.nodes) {
                Point p1 = new Point((int)(node.position.x*scale), (int)(node.position.y*scale));
                Point p2 = new Point((int)((node.position.x + node.velocity.x * 0.1)*scale), (int)((node.position.y + node.velocity.y * 0.1)*scale));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

    }

}
