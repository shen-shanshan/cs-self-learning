/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 236.二叉树的最近公共祖先
 */
public class Solution236 {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }

        if (root == p || root == q) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        // p、q 不在当前子树
        if (left == null && right == null) {
            return null;
        }

        // p、q 在当前节点一左一右
        if (left != null && right != null) {
            return root;
        }

        // p、q 都在其中一个子树
        if (left != null) {
            return left;
        }

        return right;
    }
}
