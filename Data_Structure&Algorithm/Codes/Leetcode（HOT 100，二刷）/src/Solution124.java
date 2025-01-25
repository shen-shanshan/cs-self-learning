/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 124.二叉树中的最大路径和
 */
public class Solution124 {

    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        dfs(root);
        return max;
    }

    public int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = dfs(root.left);
        int right = dfs(root.right);

        int val = root.val;
        int sum = root.val;

        if (left > 0) {
            val = Math.max(val, root.val + left);
            sum += left;
        }
        if (right > 0) {
            val = Math.max(val, root.val + right);
            sum += right;
        }

        max = Math.max(max, sum);
        // System.out.println("root: " + root.val + ", path: " + val + ", maxSum: " + sum);

        return val;
    }
}
