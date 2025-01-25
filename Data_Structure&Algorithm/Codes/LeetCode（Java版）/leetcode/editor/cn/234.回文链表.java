package editor.cn;

// 234.回文链表
class Solution234 {

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
        public boolean isPalindrome(ListNode head) {

            // 将链表从中部拆分
            ListNode slow = head;
            ListNode fast = head;
            ListNode last = new ListNode();
            last.next = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                last = last.next;
            }
            last.next = null;

            // 反转链表
            ListNode pre = null;
            while (slow != null) {
                ListNode next = slow.next;
                slow.next = pre;
                pre = slow;
                slow = next;
            }

            // 遍历链表
            while (head != null) {
                if (head.val != pre.val) {
                    return false;
                }
                head = head.next;
                pre = pre.next;
            }

            return true;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}