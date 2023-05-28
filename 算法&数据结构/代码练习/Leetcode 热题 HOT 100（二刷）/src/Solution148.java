/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 148.排序链表
 */
public class Solution148 {
    public ListNode sortList(ListNode head) {
        if (head == null) {
            return null;
        }
        return mySort(head, null);
    }

    public ListNode mySort(ListNode head, ListNode tail) {
        if (head == null) {
            return null;
        }

        // 终止条件：只有一个节点
        if (head.next == tail) {
            // 断尾！
            head.next = null;
            return head;
        }

        // 二分
        ListNode slow = head;
        ListNode fast = head;

        while (fast != tail && fast.next != tail) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode l1 = mySort(head, slow);
        ListNode l2 = mySort(slow, tail);

        // 归并
        return merge(l1, l2);
    }

    public ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();

        ListNode head1 = l1;
        ListNode head2 = l2;

        ListNode cur = dummy;

        while (head1 != null && head2 != null) {
            if (head1.val <= head2.val) {
                cur.next = head1;
                head1 = head1.next;
            } else {
                cur.next = head2;
                head2 = head2.next;
            }
            cur = cur.next;
        }

        if (head1 != null) {
            cur.next = head1;
        }
        if (head2 != null) {
            cur.next = head2;
        }

        return dummy.next;
    }
}
