public class Solution_876 {
    public ListNode middleNode(ListNode head) {
        if (head == null) return null;
        ListNode res = head;
        int flag = 0;
        while (head != null) {
            head = head.next;
            if (flag % 2 == 1) {
                res = res.next;
            }
            flag++;
        }
        return res;
    }

    public static void main(String[] args) {
        ListNode l6 = new ListNode(6, null);
        ListNode l5 = new ListNode(5, l6);
        // ListNode l5 = new ListNode(5, null);
        ListNode l4 = new ListNode(4, l5);
        ListNode l3 = new ListNode(3, l4);
        ListNode l2 = new ListNode(2, l3);
        ListNode l1 = new ListNode(1, l2);

        Solution_876 s = new Solution_876();
        ListNode res = s.middleNode(l1);
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }
}
