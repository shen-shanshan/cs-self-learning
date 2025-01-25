/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        // 复制每一个节点
        while (cur != null) {
            Node next = cur.next;
            cur.next = new Node(cur.val);
            cur.next.next = next;
            cur = next;
        }
        // 找到复制节点的 random 指针指向的节点
        cur = head;
        while (cur != null) {
            Node next = cur.next;
            if (cur.random != null) {
                next.random = cur.random.next;
            } else {
                next.random = null;
            }
            cur = next.next;
        }
        // 连通复制链表
        Node ans = head.next;
        cur = head;
        while (cur != null) {
            Node dupNode = cur.next;
            // 还原原链表
            cur.next = dupNode.next;
            dupNode.next = cur.next == null ? null : cur.next.next;
            cur = cur.next;
        }
        return ans;
    }
}
