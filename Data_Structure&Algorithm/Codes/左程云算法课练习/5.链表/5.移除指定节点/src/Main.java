/*
题目描述：
给定一个值，从左向右遍历链表，移除第一个节点值为该值的节点，返回删除节点后的链表的头节点
*/

public class Main {
    static Node remove(Node head, int value) {
        if (head == null) {
            System.out.println("链表为空");
            return null;
        }
        // 哑节点
        Node pre = new Node(0, head);
        pre.next = head;
        // 双指针遍历
        Node cur = head;
        Node last = pre;
        int flag = 0;
        while (cur != null) {
            if (cur.value == value) {
                last.next = cur.next;
                flag = 1;
                break;
            }
            cur = cur.next;
            last = last.next;
        }
        /*if (flag == 0) {
            System.out.println("不存在需要删除的节点");
        }*/
        return pre.next;
    }

    public static void main(String[] args) {
        int testTimes = 3;
        for (int i = 0; i < testTimes; i++) {
            // 随机生成链表
            Node head = LinkedListRandomGenerator.getInstance().generate();
            if (head == null) {
                System.out.println("原链表为空");
            } else {
                Node cur = head;
                System.out.print("原链表：");
                while (cur.next != null) {
                    System.out.print(cur.value + "->");
                    cur = cur.next;
                }
                System.out.println(cur.value);
            }
            // 随机生成数字
            int k = (int) (Math.random() * 9 + 1); // 1 ~ 9
            System.out.println("value：" + k);
            // 合并链表
            Node res = Main.remove(head, k);
            if (res == null) {
                System.out.println("移除节点后的链表为空");
            } else {
                System.out.print("移除节点后的链表：");
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
