import java.util.HashSet;
import java.util.Set;

// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution_141 {

    /* 双指针（耗时久）：
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode tmp = head;
        while (tmp.next != null) {
            if (tmp.next == tmp) {
                return true;
            }
            ListNode cycle = head;
            while (tmp.next != cycle && cycle != tmp) {
                cycle = cycle.next;
            }
            if (cycle != tmp) {
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }*/

    /* 哈希表（占用额外空间）：
    public boolean hasCycle(ListNode head) {
        Set<ListNode> seen = new HashSet<ListNode>();
        while (head != null) {
            if (!seen.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }*/

    /* 快慢指针：
    Floyd判圈算法（龟兔赛跑算法）
    我们定义两个指针，一快一慢。慢指针每次只移动一步，而快指针每次移动两步。
    初始时，慢指针在位置 head，而快指针在位置 head.next。
    这样一来，如果在移动的过程中，快指针反过来追上慢指针，就说明该链表为环形链表。
    否则快指针将到达链表尾部，该链表不为环形链表。*/
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            // 慢指针每次只移动一步，而快指针每次移动两步
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

    public static void main(String[] args) {
        // 初始化链表
        ListNode l1 = new ListNode(3);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(0);
        ListNode l4 = new ListNode(-4);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l2;

        /*ListNode l1 = new ListNode(1);
        l1.next = l1;*/

        Solution_141 s = new Solution_141();
        boolean res = s.hasCycle(l1);
        System.out.println(res);
    }
}
