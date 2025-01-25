/**
 * @BelongsProject: 代码随想录
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

        // 若 p、q 在两边，则当前节点就是结果
        if ((left == p && right == q) || (left == q && right == p)) {
            return root;
        }

        if (left != null && right == null) {
            return left;
        }
        if (left == null && right != null) {
            return right;
        }

        return null;
    }
}
