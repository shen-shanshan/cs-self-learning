// 深度优先遍历
public class Solution {
    public int pathSum(TreeNode root, int targetSum) {
        // base case
        if (root == null) {
            return 0;
        }
        int count = 0;
        // 选中当前节点
        count += dfs(root, targetSum);
        // 不选当前节点
        count += pathSum(root.left, targetSum);
        count += pathSum(root.right, targetSum);
        return count;
    }

    public int dfs(TreeNode root, int targetSum) {
        // base case
        if (root == null) {
            return 0;
        }
        int count = 0;
        if (root.val == targetSum) {
            count++;
        }
        count += dfs(root.left, targetSum - root.val);
        count += dfs(root.right, targetSum - root.val);
        return count;
    }
}