package eliascregard.physics;

public class SpringBody {

    public Node[] nodes;
    public Spring[] springs;

    public SpringBody(Node[] nodes, Spring[] springs) {
        this.nodes = nodes;
        this.springs = springs;
    }

    public void update(double deltaTime, Vector2D gravity) {
        for (Node node : this.nodes) {
            node.update(deltaTime, gravity);
        }
        for (Spring spring : this.springs) {
            spring.update(deltaTime);
        }
    }

    public static SpringBody makeRectangle(double x, double y, int width, int height, double nodeMass, double springStiffness, double springDampingFactor) {
        Node[] nodes = new Node[width * height];
        Spring[] springs = new Spring[(width - 1) * height + (height-1) * width + (height - 1) * (width - 1) * 2];

        int nodeIndex = 0;
        int springIndex = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nodes[nodeIndex] = new Node(new Vector2D(x + i * 27, y+Node.NODE_RADIUS + j * 27), nodeMass);
                nodeIndex++;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height-1; j++) {
                springs[springIndex] = new Spring(nodes[i + j * width], nodes[i + (j + 1) * width], springStiffness, springDampingFactor);
                springIndex++;
            }
        }
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height; j++) {
                springs[springIndex] = new Spring(nodes[i + j * width], nodes[i + 1 + j * width], springStiffness, springDampingFactor);
                springIndex++;
            }
        }
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                springs[springIndex] = new Spring(nodes[i + j * width], nodes[i + 1 + (j + 1) * width], springStiffness, springDampingFactor);
                springIndex++;
                springs[springIndex] = new Spring(nodes[i + 1 + j * width], nodes[i + (j + 1) * width], springStiffness, springDampingFactor);
                springIndex++;
            }
        }

        return new SpringBody(nodes, springs);
    }

}
