package eliascregard.physics;

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
            if (spring.getLength() > 2 * spring.restLength) {
                this.removeSpring(spring);
            }
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
        if (y < 0) {y = 0;}

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

}
