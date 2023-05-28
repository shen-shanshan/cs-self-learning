/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 235.二叉搜索树的最近公共祖先
 */
public class Solution235 {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }

        // 当前节点就是 p 或 q
        if (root.val == p.val || root.val == q.val) {
            return root;
        }

        // p、q 分别位于两侧
        if ((p.val < root.val && root.val < q.val) || (q.val < root.val && root.val < p.val)) {
            return root;
        }

        TreeNode left = null;
        TreeNode right = null;

        if (Math.max(p.val, q.val) < root.val) {
            left = lowestCommonAncestor(root.left, p, q);
        }
        if (Math.min(p.val, q.val) > root.val) {
            right = lowestCommonAncestor(root.right, p, q);
        }

        if (left != null) {
            return left;
        }
        if (right != null) {
            return right;
        }

        // left = right = null
        return null;
    }
}
