/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    Node head = null;

    Node pre = null;

    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        dfs(root);
        // 连接头尾节点，此时 pre 就是链表的 tail
        head.left = pre;
        pre.right = head;
        return head;
    }

    // 递归（中序遍历）
    public void dfs(Node root) {
        // base case
        if (root == null) {
            return;
        }
        // 递归左子树
        dfs(root.left);
        // 连接当前节点
        if (pre == null) {
            head = root;
        } else {
            pre.right = root;
            root.left = pre;
        }
        pre = root;
        // 递归右子树
        dfs(root.right);
    }
}
