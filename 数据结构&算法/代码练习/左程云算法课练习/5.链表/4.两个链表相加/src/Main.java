/*
题目描述：
给出两个链表的头节点head1和head2，链表的节点值从左到右代表某个数字的从低位到高位，返回相加之后的链表。
示例：
链表1：4->3->6
链表2：2->5->3
返回： 6->8->9 （解释：634 + 352 = 986）
注意：相加时需要考虑进位。
*/

public class Main {
    static Node LinkedListAdd(Node head1, Node head2) {
        if (head1 == null && head2 == null) return null;
        if (head1 == null) return head2;
        if (head2 == null) return head1;

        int len1 = 1;
        Node cur1 = head1;
        while (cur1.next != null) {
            cur1 = cur1.next;
            len1++;
        }
        System.out.println("len1 = " + len1);
        int len2 = 1;
        Node cur2 = head2;
        while (cur2.next != null) {
            cur2 = cur2.next;
            len2++;
        }
        System.out.println("len2 = " + len2);

        Node headL;
        Node headS;
        Node res;
        if (len1 >= len2) {
            headL = head1;
            headS = head2;
            res = head1;
        } else {
            headL = head2;
            headS = head1;
            res = head2;
        }
        Node last = null;

        /*Node l = len1 >= len2 ? head1:head2;
        Node s = l == head1 ? head2:head1;
        Node headL = l;
        Node headS = s;
        Node last = headL;*/

        // 进位信息
        int add = 0;
        // 第一阶段，将短链表的节点值依次加到长链表上，并计算进位信息
        while (headS != null) {
            int sum = headL.value + headS.value + add;
            headL.value = sum % 10;
            add = sum / 10;
            last = headL;
            headL = headL.next;
            headS = headS.next;
        }

        // 第二阶段，将进位信息依次与长链表的节点值相加，并产生新的进位信息
        while (headL != null) {
            int sum = headL.value + add;
            headL.value = sum % 10;
            add = sum / 10;
            last = headL;
            headL = headL.next;
        }

        if (add != 0) {
            last.next = new Node(1);
        }

        return res;
    }

    public static void main(String[] args) {
        int testTimes = 1;
        for (int i = 0; i < testTimes; i++) {
            // 随机生成链表
            Node head1 = LinkedListRandomGenerator.getInstance().generate();
            Node head2 = LinkedListRandomGenerator.getInstance().generate();
            Node cur1 = head1;
            Node cur2 = head2;
            // 打印链表
            System.out.print("链表1：");
            while (cur1.next != null) {
                System.out.print(cur1.value + "->");
                cur1 = cur1.next;
            }
            System.out.println(cur1.value);
            System.out.print("链表2：");
            while (cur2.next != null) {
                System.out.print(cur2.value + "->");
                cur2 = cur2.next;
            }
            System.out.println(cur2.value);
            // 合并链表
            Node res = Main.LinkedListAdd(head1, head2);
            // 打印链表
            System.out.print("合并后的链表：");
            while (res.next != null) {
                System.out.print(res.value + "->");
                res = res.next;
            }
            System.out.println(res.value);
            System.out.println("---------------------");
        }
    }
}
