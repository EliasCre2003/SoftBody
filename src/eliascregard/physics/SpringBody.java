package eliascregard.physics;

import eliascregard.math.vectors.Vector2;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SpringBody {

    public Node[] nodes;
    public Spring[] springs;

    public SpringBody(Node[] nodes, Spring[] springs) {
        this.nodes = nodes;
        this.springs = springs;
    }
    public void update(double deltaTime, Vector2 gravity) {
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
                nodes[nodeIndex] = new Node(new Vector2(
                        x + i * (2*nodeRadius + spacing), y+nodeRadius + j * (2*nodeRadius + spacing)),
                        nodeMass, nodeRadius);
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
        springStiffness = Math.sqrt(2) * springStiffness;
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
        Spring[] springs = new Spring[edgesForTriangulatedTriangle(size)];
        double centerSpacing = spacing + 2 * nodeRadius;
        int nodeIndex = 0;
        double verticalStep = centerSpacing * Math.sin(Math.PI / 3);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i + 1; j++) {
                Vector2 position = new Vector2(x - i * centerSpacing / 2 + j * centerSpacing, y + i * verticalStep);
                nodes[nodeIndex] = new Node(position, nodeMass, nodeRadius);
                nodeIndex++;
            }
        }
        int springIndex = 0, currentNode = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < i + 1; j++) {
                springs[springIndex] = new Spring(
                        nodes[currentNode], nodes[currentNode + i + 1], springStiffness, springDampingFactor
                );
                springs[springIndex + 1] = new Spring(
                        nodes[currentNode], nodes[currentNode + i + 2], springStiffness, springDampingFactor
                );
                springs[springIndex + 2] = new Spring(
                        nodes[currentNode + i + 1], nodes[currentNode + i + 2], springStiffness, springDampingFactor
                );
                springIndex += 3;
                currentNode++;
            }
        }
        return new SpringBody(nodes, springs);


    }

    public static SpringBody simpleCircle(double x, double y, double radius, int segments, double nodeMass,
                                          double springStiffness, double springDampingFactor, double nodeRadius) {
        Node[] nodes = new Node[segments + 1];
        Spring[] springs = new Spring[segments * 2];

        nodes[0] = new Node(new Vector2(x, y), (segments / 2.0) * nodeMass, nodeRadius);
        double theta = 0;
        for (int i = 0; i < segments; i++) {
            nodes[i+1] = new Node(new Vector2(
                    x + radius * Math.cos(theta), y + radius * Math.sin(theta)), nodeMass, nodeRadius
            );
            theta += 2 * Math.PI / segments;
        }
        for (int i = 0; i < segments; i++) {
            springs[i] = new Spring(nodes[0], nodes[i+1], springStiffness, springDampingFactor);
            springs[i+segments] = new Spring(nodes[i+1], nodes[(i+1) % segments + 1], 100 * springStiffness, 10 * springDampingFactor);
        }

        return new SpringBody(nodes, springs);
    }

    public static SpringBody homogeneousHexagon(double x, double y, int layers, double nodeMass, double springStiffness,
                                               double springDampingFactor, double spacing, double nodeRadius) {
        // NODES
        Node[] nodes = new Node[3 * layers * (layers - 1) + 1];
        nodes[0] = new Node(new Vector2(x, y), nodeMass, nodeRadius);
        double centerSpacing = spacing + 2 * nodeRadius;
        double verticalLayerSpacing = centerSpacing * Math.sin(Math.PI / 3);
        int nodeIndex = 1;

        for (int i = 0; i < layers; i++) {
            double currentY = y - (layers - i - 1) * verticalLayerSpacing;
            double startX = x - (layers - 1 + i) * centerSpacing / 2;
            for (int j = 0; j < layers + i; j++) {
                if (i == layers - 1 && j == layers - 1) continue;
                nodes[nodeIndex] = new Node(new Vector2(startX + j * centerSpacing, currentY), nodeMass, nodeRadius);
                nodeIndex++;
            }
        }
        for (int i = 0; i < layers - 1; i++) {
            double currentY = y + (i + 1) * verticalLayerSpacing;
            double startX = x - (2 * layers - i - 3) * centerSpacing / 2;
            for (int j = 0; j < 2 * layers - 2 - i; j++) {
                nodes[nodeIndex] = new Node(new Vector2(startX + j * centerSpacing, currentY), nodeMass, nodeRadius);
                nodeIndex++;
            }
        }

        // SPRINGS
        Spring[] springs = new Spring[edgesForTriangulatedHexagon(layers)];

        for (int i = 0; i < layers - 1; i++) {
            springs[i] = new Spring(nodes[i+1], nodes[i+2], springStiffness, springDampingFactor);
        }
        int springIndex = layers - 1;
        int currentNodeIndex = 1;
        for (int i = 0; i < layers - 1; i++) {
            for (int j = 0; j < layers + i; j++) {
                int node1 = currentNodeIndex + j;
                int node2 = node1 + layers + i;
                int node3 = node2 + 1;
                if (node2 == (nodes.length - 1) / 2 + 1) node2 = 0;
                else if (node2 > (nodes.length - 1) / 2 + 1) node2 -= 1;
                if (node3 == (nodes.length - 1) / 2 + 1) node3 = 0;
                else if (node3 > (nodes.length - 1) / 2 + 1) node3 -= 1;
                springs[springIndex] = new Spring(nodes[node1], nodes[node2], springStiffness, springDampingFactor);
                springs[springIndex + 1] = new Spring(nodes[node1], nodes[node3], springStiffness, springDampingFactor);
                springs[springIndex + 2] = new Spring(nodes[node2], nodes[node3], springStiffness, springDampingFactor);
                springIndex += 3;
            }
            currentNodeIndex += layers + i;

        }

        for (int i = 0; i < layers - 1; i++) {
            springs[springIndex] = new Spring(
                    nodes[nodes.length - (i + 1)], nodes[nodes.length - (i + 2)],
                    springStiffness, springDampingFactor
            );
            springIndex++;
        }
        currentNodeIndex = 1;
        for (int i = 0; i < layers - 2; i++) {
            for (int j = 0; j < layers + i; j++) {
                int node1 = nodes.length - (currentNodeIndex + j);
                int node2 = node1 - (layers + i);
                int node3 = node2 - 1;
                springs[springIndex] = new Spring(nodes[node1], nodes[node2], springStiffness, springDampingFactor);
                springs[springIndex + 1] = new Spring(nodes[node1], nodes[node3], springStiffness, springDampingFactor);
                springs[springIndex + 2] = new Spring(nodes[node2], nodes[node3], springStiffness, springDampingFactor);
                springIndex += 3;
            }
            currentNodeIndex += layers + i;
        }
        currentNodeIndex = (nodes.length - 1) / 2 - layers + 2;
        for (int i = 0; i < 2 * (layers - 1); i++) {
            int node1 = currentNodeIndex + i;
            int node2 = node1 + 2 * (layers - 1);
            int node3 = node1 + 1;
            if (node1 == (nodes.length - 1) / 2 + 1) node1 = 0;
            else if (node1 > (nodes.length - 1) / 2 + 1) node1 -= 1;
            if (node3 == (nodes.length - 1) / 2 + 1) node3 = 0;
            else if (node3 > (nodes.length - 1) / 2 + 1) node3 -= 1;
            springs[springIndex] = new Spring(nodes[node1], nodes[node2], springStiffness, springDampingFactor);
            springs[springIndex + 1] = new Spring(nodes[node2], nodes[node3], springStiffness, springDampingFactor);
            springIndex += 2;

        }

        return new SpringBody(nodes, springs);

    }

    private static int edgesForTriangulatedTriangle(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        return 3 * (size * (size - 1)) / 2;
    }
    private static int edgesForTriangulatedHexagon(int layers) {
        if (layers < 0) {
            throw new IllegalArgumentException("layers must be non-negative");
        }
        if (layers <= 1) {
            return 0;
        }
        return edgesForTriangulatedHexagon(layers - 1) + 18 * (layers - 1) - 6;
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

    public void saveAs(String path) {
        try {
            File saveFile = new File(path);
            if (saveFile.createNewFile()) {
                System.out.println("File created: " + saveFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            StringBuilder bodyString = new StringBuilder();
            for (Node node : nodes) {
                String newNode =
                        node.position.getX() + " " + node.position.getY() + " " + node.mass + " " + node.radius + " "
                        + node.isFixed + "; ";
                bodyString.append(newNode);
            }
            bodyString.append("\n");
            List<Node> nodesList = Arrays.asList(nodes);
            for (Spring spring : springs) {
                String newSpring =
                        nodesList.indexOf(spring.node1) + " " + nodesList.indexOf(spring.node2) + " " +
                        spring.stiffness + " " + spring.dampingFactor + "; ";
                bodyString.append(newSpring);
            }
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(bodyString.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Body couldn't be saved for some reason.");
            e.printStackTrace();
        }
    }

    public static SpringBody load(String path) {
        try {
            File bodyFile = new File(path);
            Scanner fileReader = new Scanner(bodyFile);
            String[] nodesStrings = fileReader.nextLine().split("; ");
            Node[] nodes = new Node[nodesStrings.length];
            for (int i = 0; i < nodesStrings.length; i++) {
                String[] nodeStrings = nodesStrings[i].split(" ");
                nodes[i] = new Node(
                        new Vector2(Double.parseDouble(nodeStrings[0]), Double.parseDouble(nodeStrings[1])),
                        Double.parseDouble(nodeStrings[2]), Double.parseDouble(nodeStrings[3]),
                        Boolean.parseBoolean(nodeStrings[4])
                );
            }
            String[] springsStrings = fileReader.nextLine().split("; ");
            Spring[] springs = new Spring[springsStrings.length];
            for (int i = 0; i < springsStrings.length; i++) {
                String[] springStrings = springsStrings[i].split(" ");
                springs[i] = new Spring(
                        nodes[Integer.parseInt(springStrings[0])],
                        nodes[Integer.parseInt(springStrings[1])],
                        Double.parseDouble(springStrings[2]),
                        Double.parseDouble(springStrings[3])
                );
            }
            return new SpringBody(nodes, springs);
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + path + "\" was not found");
            return null;
        }
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
                Point p1 = new Point((int)(node.position.getX()*scale), (int)(node.position.getY()*scale));
                Point p2 = new Point((int)((node.position.getX() + node.velocity.getX() * 0.1)*scale),
                        (int)((node.position.getY() + node.velocity.getY() * 0.1)*scale));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(edgesForTriangulatedHexagon(3));
    }
}
