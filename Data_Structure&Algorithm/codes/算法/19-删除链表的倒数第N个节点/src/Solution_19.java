public class Solution_19 {
    /*public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head == null || head.next == null) return null;
        ListNode fast = head;
        ListNode slow = head;
        int flag = 0;
        while (fast.next.next != null) {
            fast = fast.next;
            if (flag >= n - 1) {
                slow = slow.next;
            }
            flag++;
        }
        // 删除的是头结点的情况
        if(flag < n-1) return head.next;
        // 正常情况
        slow.next = slow.next.next;
        return head;
    }*/

    // 使用哑节点改进
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        if (head == null || head.next == null) return null;
        ListNode fast = head;
        ListNode slow = dummy;
        int flag = 0;
        while (fast.next != null) {
            fast = fast.next;
            if (flag >= n - 1) {
                slow = slow.next;
            }
            flag++;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }

    public static void main(String[] args) {
         ListNode l5 = new ListNode(5, null);
         ListNode l4 = new ListNode(4, l5);
         ListNode l3 = new ListNode(3, l4);
         ListNode l2 = new ListNode(2, l3);
         ListNode l1 = new ListNode(1, l2);

        // ListNode l2 = new ListNode(2, null);
        // ListNode l1 = new ListNode(1, l2);

        Solution_19 s = new Solution_19();
        ListNode res = s.removeNthFromEnd(l1, 5);
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }
}
