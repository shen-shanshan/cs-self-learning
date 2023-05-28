public class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            // 慢指针每次只移动一步，而快指针每次移动两步
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }
}
