// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution_83 {

    /*public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode pre = head;
        while (pre.next != null) {
            ListNode tmp = pre.next;
            while (tmp.val == pre.val && tmp.next != null) {
                tmp = tmp.next;
            }
            // 如果 tmp 还没有走到链表尾部
            if (tmp.next != null) {
                pre.next = tmp;
                pre = tmp;
            }
            // 如果已经走到了链表尾部
            else if (tmp.val == pre.val) {
                pre.next = null;
            } else {
                pre.next = tmp;
                pre = tmp;
            }
        }
        return head;
    }*/

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.val == cur.next.val) {
                // 若元素重复，则删除 cur 后面的节点
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(1);
        ListNode l3 = new ListNode(2);
        ListNode l4 = new ListNode(3);
        ListNode l5 = new ListNode(3);
        ListNode l6 = new ListNode(4);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;
        l5.next = l6;

        Solution_83 s = new Solution_83();
        ListNode res = s.deleteDuplicates(l1);
        while (res.next != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
        System.out.println(res.val);
    }
}
