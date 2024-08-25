import javax.swing.tree.TreeNode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution_116 {
    // 广度优先搜索：
    // 时间复杂度：O(N)。每个节点会被访问一次且只会被访问一次，即从队列中弹出，并建立 next 指针。
    // 空间复杂度：O(N)。广度优先遍历的复杂度取决于一个层级上的最大元素数量。这种情况下空间复杂度为 O(N)。
    public Node connect1(Node root) {
        if (root == null) return null;
        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        /*while (!q.isEmpty()) {
            Deque<Node> list = new LinkedList<>();
            while (!q.isEmpty()) {
                Node tmp = q.poll();
                list.add(tmp);
            }
            while (!list.isEmpty()) {
                Node tmp = list.poll();
                if (tmp.left != null) {
                    q.offer(tmp.left);
                    q.offer(tmp.right);
                }
                if (!list.isEmpty()) {
                    Node next = list.poll();
                    tmp.next = next;
                    list.addFirst(next);
                }
            }
        }*/
        // 改进：
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                Node node = q.poll();
                if (i < size - 1) {
                    node.next = q.peek();
                }
                if (node.left != null) {
                    q.add(node.left);
                    q.add(node.right);
                }
            }
        }
        return root;
    }

    // 深度优先搜索：
    // 时间复杂度：O(N)，每个节点只访问一次。
    // 空间复杂度：O(1)，不需要存储额外的节点。
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        if (root.left != null) {
            root.left.next = root.right;
            root.right.next = (root.next != null) ? root.next.left : null;
            connect(root.left);
            connect(root.right);
        }
        return root;
    }

    public static void main(String[] args) {
        // 完美二叉树：所有叶子节点都在同一层，每个父节点都有两个子节点。
        Node n4 = new Node(4, null, null, null);
        Node n5 = new Node(5, null, null, null);
        Node n6 = new Node(6, null, null, null);
        Node n7 = new Node(7, null, null, null);
        Node n2 = new Node(2, n4, n5, null);
        Node n3 = new Node(3, n6, n7, null);
        Node n1 = new Node(1, n2, n3, null);

        Solution_116 s = new Solution_116();
        Node res = s.connect1(n1);
        // 打印
        Queue<Node> q = new LinkedList<>();
        q.offer(res);
        while (!q.isEmpty()) {
            List<Node> l = new LinkedList<>();
            while (!q.isEmpty()) {
                Node tmp = q.poll();
                l.add(tmp);
            }
            for (Node tmp : l) {
                System.out.print(tmp.next + " ");
                if (tmp.left != null) q.offer(tmp.left);
                if (tmp.right != null) q.offer(tmp.right);
            }
            System.out.println();
        }
    }
}
