package editor.cn;

// 142.环形链表 II
class Solution142 {

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) {
     * val = x;
     * next = null;
     * }
     * }
     */
    public class Solution {
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
    //leetcode submit region end(Prohibit modification and deletion)

}