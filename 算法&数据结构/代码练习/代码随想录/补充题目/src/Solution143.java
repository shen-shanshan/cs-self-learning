/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 143.重排链表
 */
public class Solution143 {
    public void reorderList(ListNode head) {
        // 快慢指针
        ListNode slow = head;
        ListNode fast = head;
        ListNode last = new ListNode();
        last.next = head;

        while (fast != null && fast.next != null) {
            last = last.next;
            slow = slow.next;
            fast = fast.next.next;
        }

        // 第一段：head 到 slow 的上一个节点
        // 第二段：slow 到 fast
        last.next = null;

        // 反转第二段链表
        ListNode head2 = reverse(slow);

        // 双指针交叉遍历
        while (head != null && head2 != null) {
            ListNode next1 = head.next;
            ListNode next2 = head2.next;
            head.next = head2;
            head2.next = next1;
            head = next1;
            head2 = next2;
        }

        // 当链表长度为奇数
        if (head2 != null) {
            head2.next = head2;
        }
    }

    public ListNode reverse(ListNode head) {
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
