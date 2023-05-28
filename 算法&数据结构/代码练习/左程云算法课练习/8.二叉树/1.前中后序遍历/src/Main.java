import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
题目描述：
前序遍历：头->左->右
中序遍历：左->头->右
后序遍历：左->右->头
*/
public class Main {

    public static void main(String[] args) {
        /*int testTimes = 5;
        for (int i = 0; i < testTimes; i++) {
            Node head = TreeRandomGenerator.getInstance().generate();
            Main.print(head);
            System.out.println("-------------------");
        }*/


    }

    // 层序遍历并打印
    public static void print(Node head) {
        if (head == null) return;

        Queue<Node> q = new LinkedList<>();
        q.offer(head);
        while (!q.isEmpty()) {
            ArrayList<Node> a = new ArrayList<>();
            while (!q.isEmpty()) {
                Node tmp = q.poll();
                a.add(tmp);
            }
            for (Node x : a) {
                System.out.print(x.value);
                if (x.left != null) q.offer(x.left);
                if (x.right != null) q.offer(x.right);
            }
            // System.out.println();
        }
    }
}
