package editor.cn;

// 92.反转链表 II
class Solution92 {

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {
        public ListNode reverseBetween(ListNode head, int left, int right) {

            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode last = dummy;

            for (int i = 1; i < left; i++) {
                head = head.next;
                last = last.next;
            }

            // 找到要反转的范围
            ListNode start = head;
            for (int i = left; i < right; i++) {
                head = head.next;
            }
            ListNode end = head;

            // 反转链表
            ListNode pre = end.next;
            end.next = null;
            while (start != null) {
                ListNode next = start.next;
                start.next = pre;
                pre = start;
                start = next;
            }

            // 连接
            last.next = pre;
            return dummy.next;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}