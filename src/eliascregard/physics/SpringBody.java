package eliascregard.physics;

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
            spring.update(deltaTime);
        }
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

}