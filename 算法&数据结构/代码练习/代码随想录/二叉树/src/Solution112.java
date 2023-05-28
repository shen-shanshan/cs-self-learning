/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 112.路径总和
 */
public class Solution112 {

    int pathSum = 0;

    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }

        return backtrack(root, targetSum);
    }

    public boolean backtrack(TreeNode root, int targetSum) {
        // 结束条件：叶子节点
        if (root.left == null && root.right == null) {
            if (pathSum + root.val == targetSum) {
                return true;
            }
            return false;
        }

        pathSum += root.val;

        if (root.left != null && backtrack(root.left, targetSum)) {
            return true;
        }
        if (root.right != null && backtrack(root.right, targetSum)) {
            return true;
        }

        // 回溯
        pathSum -= root.val;

        return false;
    }
}
