/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 234.回文链表
 */
public class Solution234 {
    public boolean isPalindrome(ListNode head) {
        ListNode dummy = new ListNode();
        dummy.next = head;

        ListNode last = dummy;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            last = last.next;
            slow = slow.next;
            fast = fast.next.next;
        }

        // 第一段：head -> last
        // 第二段：slow -> tail
        last.next = null;

        ListNode head2 = reverse(slow);

        while (head != null) {
            if (head.val != head2.val) {
                return false;
            }
            head = head.next;
            head2 = head2.next;
        }

        return true;
    }

    public ListNode reverse(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode pre = null;

        while (head != null) {
            ListNode next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        return pre;
    }
}
