public class Node {
    int value;
    Node left;
    Node right;

    Node() {
    }

    Node(int value) {
        this.value = value;
    }

    Node(int value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
}
