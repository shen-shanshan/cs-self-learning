/*
题目描述：
给定一个单链表的头节点head，和一个正数k，实现k个节点的小组内部逆序，若最后一组不足k个，则不调整。
示例：
调整前：1->2->3->4->5->6->7->8, k=3
调整后：3->2->1->6->5->4->7->8
*/

public class Main {
    public static Node kGroupReverse(Node head, int k) {
        // 获取第一组的 k 个节点，并反转
        Node start = head;
        Node end = start;
        for (int i = 0; i < k - 1; i++) {
            // 若链表长度不足 k 个，则直接返回
            if (end.next == null) return head;
            end = end.next;
        }
        Main.reverse(start, end);
        // 记录上一组的结尾
        Node lastEnd = start;
        head = end;

        // 循环获取下一组
        while (lastEnd.next != null) {
            start = lastEnd.next;
            end = start;
            for (int i = 0; i < k - 1; i++) {
                // 若剩余链表长度不足 k 个，则直接返回
                if (end.next == null) return head;
                end = end.next;
            }
            Main.reverse(start, end);
            // 连接上一组
            lastEnd.next = end;
            lastEnd = start;
        }
        return head;
    }

    public static void reverse(Node start, Node end) {
        Node tail = end.next;
        Node pre = end.next;
        while (start != tail) {
            Node next = start.next;
            start.next = pre;
            pre = start;
            start = next;
        }
        // return pre;
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

        // k 个节点的组内逆序调整
        int k = 2;
        Node head = Main.kGroupReverse(n1, k);
        // Node head = Main.reverse(n1, n4);

        // 打印结果
        while (head.next != null) {
            System.out.print(head.value + "->");
            head = head.next;
        }
        System.out.print(head.value);
    }
}
