public class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        // l1、l2 都不为空
        ListNode head = l1.val < l2.val ? l1 : l2;
        l1 = head == l1 ? l1.next : l1;
        l2 = head == l2 ? l2.next : l2;
        head.next = mergeTwoLists(l1, l2);
        return head;
    }

    public static void main(String[] args) {
        // list1
        ListNode na3 = new ListNode(4);
        ListNode na2 = new ListNode(2, na3);
        ListNode na1 = new ListNode(1, na2);

        // list2
        ListNode nb3 = new ListNode(4);
        ListNode nb2 = new ListNode(3, nb3);
        ListNode nb1 = new ListNode(1, nb2);

        Solution s = new Solution();
        ListNode ans = s.mergeTwoLists(na1, nb1);
        while (ans.next != null) {
            System.out.print(ans.val + "->");
            ans = ans.next;
        }
        System.out.println(ans.val);
    }
}
