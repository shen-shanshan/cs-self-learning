// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution_206 {

    /* 递归：
    1.时间复杂度：O(n)，其中 n 是链表的长度。需要对链表的每个节点进行反转操作。
    2.空间复杂度：O(n)，其中 n 是链表的长度。空间复杂度主要取决于递归调用的栈空间，最多为 n 层。
    public ListNode reverseList(ListNode head) {
        if (head == null) return null;
        ListNode tail = head.next;
        if (tail == null) return head;
        ListNode newHead = reverseList(head.next);
        tail.next = head;
        head.next = null;
        return newHead;
    }*/

    /* 迭代：
    1.时间复杂度：O(n)，其中 n 是链表的长度。需要遍历链表一次。
    2.空间复杂度：O(1)。*/
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        Solution_206 s = new Solution_206();
        ListNode res = s.reverseList(l1);
        while (res.next != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
        System.out.println(res.val);
    }
}
