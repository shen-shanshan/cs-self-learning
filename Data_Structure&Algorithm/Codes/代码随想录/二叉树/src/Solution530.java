/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 530.二叉搜索树的最小绝对差
 */
public class Solution530 {
    public int getMinimumDifference(TreeNode root) {
        if (root == null) {
            return 0;
        }

        dfs(root);

        return ans;
    }

    int pre = -1;

    int ans = Integer.MAX_VALUE;

    // 中序遍历
    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }

        dfs(root.left);

        if (pre == -1) {
            pre = root.val;
        } else {
            ans = Math.min(ans, root.val - pre);
            pre = root.val;
        }

        dfs(root.right);
    }
}
