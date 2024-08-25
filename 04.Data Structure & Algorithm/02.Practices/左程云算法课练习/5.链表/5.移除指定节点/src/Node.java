public class Node {
    int value;
    Node next;

    public Node() {

    }

    Node(int value) {
        this.value = value;
    }

    Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }
}

