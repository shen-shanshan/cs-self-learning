public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode cur1 = l1;
        int len1 = 0;
        while (cur1 != null) {
            len1++;
            cur1 = cur1.next;
        }

        ListNode cur2 = l2;
        int len2 = 0;
        while (cur2 != null) {
            len2++;
            cur2 = cur2.next;
        }

        // 长短链表重定向
        ListNode ll = len1 >= len2 ? l1 : l2;
        ListNode ls = ll == l1 ? l2 : l1;

        // 进位
        int add = 0;
        ListNode cur = ll;
        ListNode last = cur;
        while (ls != null) {
            int sum = cur.val + ls.val + add;
            cur.val = sum % 10;
            add = sum / 10;
            last = cur;
            cur = cur.next;
            ls = ls.next;
        }
        // 短链表遍历完了
        while (cur != null) {
            int sum = add + cur.val;
            cur.val = sum % 10;
            last = cur;
            add = sum / 10;
            cur = cur.next;
        }
        // 长链表遍历完了
        if (add == 1) {
            last.next = new ListNode(1);
        }
        return ll;
    }

    public static void main(String[] args) {
        // list 1
        ListNode na3 = new ListNode(3);
        ListNode na2 = new ListNode(4, na3);
        ListNode na1 = new ListNode(2, na2);
        // list 2
        ListNode nb3 = new ListNode(4);
        ListNode nb2 = new ListNode(6, nb3);
        ListNode nb1 = new ListNode(5, nb2);

        Solution s = new Solution();
        ListNode res = s.addTwoNumbers(na1, nb1);
        while (res.next != null) {
            System.out.print(res.val + "->");
            res = res.next;
        }
        System.out.println(res.val);
    }
}
