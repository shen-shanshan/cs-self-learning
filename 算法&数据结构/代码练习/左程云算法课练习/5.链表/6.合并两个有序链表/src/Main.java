/*
题目描述：
给定两个有序链表的头节点head1和head2，返回合并之后的大链表，要求依然有序
*/

public class Main {
    static void print(Node head) {
        if (head == null) {
            System.out.println("链表为空");
        } else {
            Node cur = head;
            System.out.print("链表当前状态为：");
            while (cur.next != null) {
                System.out.print(cur.value + "->");
                cur = cur.next;
            }
            System.out.println(cur.value);
        }
    }

    static Node addList(Node head1, Node head2) {
        if (head1 == null && head2 == null) return null;
        if (head1 == null) return head2;
        if (head2 == null) return head1;

        Node pre = new Node(0);
        Node cur = pre;
        Node cur1 = head1;
        Node cur2 = head2;

        while (cur1 != null && cur2 != null) {
            if (cur1.value >= cur2.value) {
                cur.next = cur2;
                cur2 = cur2.next;
            } else {
                cur.next = cur1;
                cur1 = cur1.next;
            }
            cur = cur.next;
            Main.print(pre.next);
        }
        if (cur1 == null) {
            cur.next = cur2;
        }
        if (cur2 == null) {
            cur.next = cur1;
        }
        return pre.next;
    }

    public static void main(String[] args) {
        int testTimes = 3;
        for (int i = 0; i < testTimes; i++) {
            // 随机生成链表1
            Node head1 = LinkedListRandomGenerator.getInstance().generate();
            if (head1 == null) {
                System.out.println("链表1为空");
            } else {
                Node cur1 = head1;
                System.out.print("链表1：");
                while (cur1.next != null) {
                    System.out.print(cur1.value + "->");
                    cur1 = cur1.next;
                }
                System.out.println(cur1.value);
            }
            // 随机生成链表1
            Node head2 = LinkedListRandomGenerator.getInstance().generate();
            if (head2 == null) {
                System.out.println("链表2为空");
            } else {
                Node cur2 = head2;
                System.out.print("链表2：");
                while (cur2.next != null) {
                    System.out.print(cur2.value + "->");
                    cur2 = cur2.next;
                }
                System.out.println(cur2.value);
            }
            // 合并链表
            Node res = Main.addList(head1, head2);
            if (res == null) {
                System.out.println("合并后的链表为空");
            } else {
                System.out.print("合并后的链表：");
                while (res.next != null) {
                    System.out.print(res.value + "->");
                    res = res.next;
                }
                System.out.println(res.value);
            }
            System.out.println("---------------------");
        }
    }
}
