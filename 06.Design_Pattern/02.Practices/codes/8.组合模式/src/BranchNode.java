import java.util.ArrayList;
import java.util.List;

public class BranchNode extends Node {
    String name;

    List<Node> nodes = new ArrayList<>();

    public BranchNode(String name) {
        this.name = name;
    }

    public void add(Node n) {
        nodes.add(n);
    }
}
