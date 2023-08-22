import javax.swing.*;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 114.二叉树展开为链表
 */
public class Solution114 {
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }

        TreeNode left = root.left;
        TreeNode right = root.right;

        // 先序遍历
        root.left = null;
        root.right = left;

        flatten(left);

        // 获取左子树的末尾节点
        TreeNode tail = root;
        while (tail.right != null) {
            tail = tail.right;
        }

        tail.right = right;

        flatten(right);
    }
}
