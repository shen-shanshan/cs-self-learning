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
        ListNode la3 = new ListNode(4, null);
        ListNode la2 = new ListNode(2, la3);
        ListNode la1 = new ListNode(1, la2);

        ListNode lb3 = new ListNode(4, null);
        ListNode lb2 = new ListNode(3, lb3);
        ListNode lb1 = new ListNode(1, lb2);

        Solution_21 s = new Solution_21();
        ListNode res = s.mergeTwoLists(la1, lb1);
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }
}
