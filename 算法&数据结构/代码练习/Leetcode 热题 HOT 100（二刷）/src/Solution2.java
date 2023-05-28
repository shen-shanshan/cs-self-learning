/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 2.两数相加
 */
public class Solution2 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode();
        ListNode cur = dummyHead;

        // 进位
        int add = 0;

        while (l1 != null && l2 != null) {
            cur.next = new ListNode();
            cur = cur.next;

            int sum = l1.val + l2.val + add;
            cur.val = sum % 10;
            add = sum / 10;

            l1 = l1.next;
            l2 = l2.next;
        }

        while (l1 != null) {
            cur.next = new ListNode();
            cur = cur.next;

            int sum = l1.val + add;
            cur.val = sum % 10;
            add = sum / 10;

            l1 = l1.next;
        }

        while (l2 != null) {
            cur.next = new ListNode();
            cur = cur.next;

            int sum = l2.val + add;
            cur.val = sum % 10;
            add = sum / 10;

            l2 = l2.next;
        }

        if (add == 1) {
            cur.next = new ListNode();
            cur = cur.next;
            cur.val = 1;
        }

        return dummyHead.next;
    }
}
