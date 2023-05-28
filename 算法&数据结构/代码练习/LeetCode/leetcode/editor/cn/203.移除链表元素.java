package editor.cn;

// 203.移除链表元素
class Solution203 {

    public static void main(String[] args) {
        Solution solution = new Solution203().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class Solution {
        public ListNode removeElements(ListNode head, int val) {
            // 创建虚拟头结点
            ListNode dummy = new ListNode(0);
            dummy.next = head;
            // 遍历
            ListNode last = dummy;
            while (head != null) {
                if (head.val == val) {
                    last.next = head.next;
                } else {
                    last = last.next;
                }
                head = head.next;
            }
            return dummy.next;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}