// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution_203 {

    /* 递归：
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        if (head.val != val) {
            head.next = removeElements(head.next, val);
            return head;
        } else if (head.next != null) {
            return removeElements(head.next, val);
        }
        return null;
    }*/

    // 迭代：
    public ListNode removeElements(ListNode head, int val) {
        // 创建虚拟头结点
        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;
        ListNode temp = dummyHead;
        // 遍历链表
        while (temp.next != null) {
            if (temp.next.val == val) {
                // 删除下一个节点
                temp.next = temp.next.next;
            } else {
                temp = temp.next;
            }
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(6);
        ListNode l4 = new ListNode(3);
        ListNode l5 = new ListNode(4);
        ListNode l6 = new ListNode(5);
        ListNode l7 = new ListNode(6);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;
        l5.next = l6;
        l6.next = l7;

        int val = 6;

        Solution_203 s = new Solution_203();
        ListNode res = s.removeElements(l1, val);
        while (res.next != null) {
            System.out.print(res.val + " ——> ");
            res = res.next;
        }
        System.out.println(res.val);
    }
}
