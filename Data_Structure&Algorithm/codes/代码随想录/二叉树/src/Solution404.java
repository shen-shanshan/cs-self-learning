/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 404.左叶子之和
 */
public class Solution404 {

    int leftSum = 0;

    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }

        dfs(root, root);

        return leftSum;
    }

    public void dfs(TreeNode root, TreeNode father) {
        // 终止条件：叶子节点
        if (root.left == null && root.right == null) {
            if (root == father.left) {
                leftSum += root.val;
            }
            return;
        }

        if (root.left != null) {
            dfs(root.left, root);
        }
        if (root.right != null) {
            dfs(root.right, root);
        }
    }
}
