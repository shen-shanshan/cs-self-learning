public class Solution {
    public int[] reversePrint(ListNode head) {
        ListNode pre = null;
        int len = 0;
        // 反转链表
        while (head != null) {
            len++;
            ListNode next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        int[] ret = new int[len];
        int i = 0;
        while (pre != null) {
            ret[i++] = pre.val;
            pre = pre.next;
        }
        return ret;
    }
}
