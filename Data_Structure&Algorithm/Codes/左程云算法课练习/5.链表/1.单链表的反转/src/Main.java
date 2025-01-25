/*
题目描述：
传入一个单链表的head，返回反转后链表的head
*/

public class Main {
    public static Node reverse(Node head) {
        Node pre = null;
        Node cur = null;
        while (head.next != null) {
            cur = head.next;
            head.next = pre;
            pre = head;
            head = cur;
        }
        head.next = pre;
        return head;
    }

    public static void main(String[] args) {
        // 初始化链表
        Node n7 = new Node(7, null);
        Node n6 = new Node(6, n7);
        Node n5 = new Node(5, n6);
        Node n4 = new Node(4, n5);
        Node n3 = new Node(3, n4);
        Node n2 = new Node(2, n3);
        Node n1 = new Node(1, n2);

        // 反转链表
        Node ans = Main.reverse(n1);

        // 打印结果
        while (ans.next != null) {
            System.out.print(ans.value + "->");
            ans = ans.next;
        }
        System.out.println(ans.value);
    }
}
