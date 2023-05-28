/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 111.二叉树的最小深度
 */
public class Solution111 {
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // 叶子节点
        if (root.left == null && root.right == null) {
            return 1;
        }

        int depth = Integer.MAX_VALUE;

        if (root.left != null) {
            depth = Math.min(depth, minDepth(root.left));
        }
        if (root.right != null) {
            depth = Math.min(depth, minDepth(root.right));
        }

        return depth + 1;
    }
}
