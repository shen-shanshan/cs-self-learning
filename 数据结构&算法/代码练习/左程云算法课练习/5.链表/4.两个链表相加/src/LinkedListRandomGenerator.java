public class LinkedListRandomGenerator {
    private final int maxLength;
    private final int maxValue;

    // 单例模式（饿汉式）
    // 1.创建实例
    private static final LinkedListRandomGenerator gen = new LinkedListRandomGenerator();

    // 2.构造私有
    private LinkedListRandomGenerator() {
        maxLength = 5;
        maxValue = 9;
    }

    // 3.提供接口
    public static LinkedListRandomGenerator getInstance() {
        return gen;
    }

    public Node generate() {
        int length = (int) (Math.random() * maxLength + 1); // 1 ~ 10
        if (length == 0) {
            return null;
        }
        Node cur = new Node((int) (Math.random() * maxValue + 1));
        Node head = cur;
        int i = 1;
        while (i < length) {
            cur.next = new Node((int) (Math.random() * maxValue + 1));
            cur = cur.next;
            i++;
        }
        cur.next = null;
        return head;
    }
}

