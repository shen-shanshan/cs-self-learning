/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public ListNode deleteNode(ListNode head, int val) {
        if (head == null) {
            return head;
        }
        // 若目标节点就是头节点（链表各节点值不重复）
        if (head.val == val) {
            return head.next;
        }
        ListNode last = new ListNode(1);
        last.next = head;
        ListNode cur = head;
        while (cur != null && cur.val != val) {
            last = last.next;
            cur = cur.next;
        }
        last.next = cur.next;
        return head;
    }
}
