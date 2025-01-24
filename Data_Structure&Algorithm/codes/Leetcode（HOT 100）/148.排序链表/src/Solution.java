public class Solution {
    public ListNode sortList(ListNode head) {
        // base case
        if (head == null || head.next == null) {
            return head;
        }
        // 拆分为两个子链表
        ListNode slow = head;
        ListNode fast = head;
        ListNode last = new ListNode();
        last.next = head;
        while (fast != null) {
            if (fast.next == null) {
                break;
            }
            // fast.next != null
            last = last.next;
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode left = head;
        ListNode right = slow;
        last.next = null;
        // 归并排序
        ListNode sortLeft = sortList(left);
        ListNode sortRight = sortList(right);
        return merge(sortLeft, sortRight);
    }

    public ListNode merge(ListNode left, ListNode right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.val <= right.val) {
            left.next = merge(left.next, right.next);
            return left;
        }
        right.next = merge(left, right.next);
        return right;
    }
}