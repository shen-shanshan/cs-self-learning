/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 337.打家劫舍 III
 */
public class Solution337 {
    public int rob(TreeNode root) {
        int[] ans = dfs(root);
        return Math.max(ans[0], ans[1]);
    }

    public int[] dfs(TreeNode root) {
        if (root == null) {
            return new int[]{0, 0};
        }

        // int[0]: 拿根节点
        // int[1]: 不拿根节点

        int[] left = dfs(root.left);
        int[] right = dfs(root.right);

        // 拿根节点
        int val1 = root.val + left[1] + right[1];
        // 不拿根节点
        int val2 = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

        return new int[]{val1, val2};
    }
}
