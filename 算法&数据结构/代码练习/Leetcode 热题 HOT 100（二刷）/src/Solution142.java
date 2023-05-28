/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 142.环形链表 II
 */
public class Solution142 {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        // 长度为 0、1 不可能成环
        if (head == null) {
            return null;
        } else if (head.next == null) {
            return null;
        }

        // 第一次相遇
        slow = slow.next;
        fast = fast.next.next;
        while (fast != null && slow != fast) {
            slow = slow.next;
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            } else {
                return null;
            }
            // System.out.println("slow: " + slow.val + ", fast: " + fast.val);
        }

        // 无环
        if (fast == null) {
            return null;
        }

        // 找到环入口
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }
}
