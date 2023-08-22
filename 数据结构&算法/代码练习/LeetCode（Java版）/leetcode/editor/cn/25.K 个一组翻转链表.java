package editor.cn;

// 25.K 个一组翻转链表
class Solution25 {

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
        public ListNode reverseKGroup(ListNode head, int k) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;

            // 上一组 k 个节点的末尾
            ListNode last = dummy;

            while (last.next != null) {
                // 找到当前这一组的头和尾
                ListNode start = last.next;
                ListNode end = last;
                int count = k;
                while (end != null && count > 0) {
                    end = end.next;
                    count--;
                }

                // 最后凑不够 k 个
                if (end == null) {
                    break;
                }

                // 能够凑满 k 个
                ListNode pre = end.next;
                end.next = null;
                ListNode cur = start;

                // 反转链表
                while (cur != null) {
                    ListNode next = cur.next;
                    cur.next = pre;
                    pre = cur;
                    cur = next;
                }

                // 拼接
                last.next = pre;

                // 更新末尾
                last = start;
            }

            return dummy.next;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}