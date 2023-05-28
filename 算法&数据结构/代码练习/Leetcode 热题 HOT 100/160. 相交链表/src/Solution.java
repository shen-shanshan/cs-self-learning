public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lenA = 0;
        int lenB = 0;
        ListNode cur = headA;
        while (cur != null) {
            cur = cur.next;
            lenA++;
        }
        cur = headB;
        while (cur != null) {
            cur = cur.next;
            lenB++;
        }
        if (lenA < lenB) {
            for (int i = 0; i < lenB - lenA; i++) {
                headB = headB.next;
            }
        } else if (lenA > lenB) {
            for (int i = 0; i < lenA - lenB; i++) {
                headA = headA.next;
            }
        }
        while (headA != null && headB != null) {
            if (headA == headB) {
                return headA;
            }
            headA = headA.next;
            headB = headB.next;
        }
        return null;
    }
}