// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution_21 {

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        }
        l2.next = mergeTwoLists(l1, l2.next);
        return l2;
    }

    public static void main(String[] args) {
        // 链表 a
        ListNode la1 = new ListNode(1);
        ListNode la2 = new ListNode(2);
        ListNode la3 = new ListNode(4);
        la1.next = la2;
        la2.next = la3;
        // 链表 b
        ListNode lb1 = new ListNode(1);
        ListNode lb2 = new ListNode(3);
        ListNode lb3 = new ListNode(4);
        lb1.next = lb2;
        lb2.next = lb3;

        Solution_21 s = new Solution_21();
        ListNode res = s.mergeTwoLists(la1, lb1);
        while (res.next != null) {
            System.out.print(res.val + " ——> ");
            res = res.next;
        }
        System.out.println(res.val);
    }
}
