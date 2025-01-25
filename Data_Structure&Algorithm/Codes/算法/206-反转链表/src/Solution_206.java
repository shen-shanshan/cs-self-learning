public class Solution_206 {
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
        ListNode la5 = new ListNode(5, null);
        ListNode la4 = new ListNode(4, la5);
        ListNode la3 = new ListNode(3, la4);
        ListNode la2 = new ListNode(2, la3);
        ListNode la1 = new ListNode(1, la2);

        Solution_206 s = new Solution_206();
        ListNode res = s.reverseList(la1);
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }
}
