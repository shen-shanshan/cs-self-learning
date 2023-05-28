import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 113.路径总和 II
 */
public class Solution113 {

    List<List<Integer>> ans = new ArrayList<>();

    List<Integer> path = new ArrayList<>();

    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return ans;
        }

        backtrack(root, 0, targetSum);

        return ans;
    }

    public void backtrack(TreeNode root, int pathSum, int targetSum) {
        path.add(root.val);

        // 叶子节点
        if (root.left == null && root.right == null) {
            if (pathSum + root.val == targetSum) {
                ans.add(new ArrayList<>(path));
            }
            path.remove(path.size() - 1);
            return;
        }

        if (root.left != null) {
            backtrack(root.left, pathSum + root.val, targetSum);
        }
        if (root.right != null) {
            backtrack(root.right, pathSum + root.val, targetSum);
        }

        // 回溯
        path.remove(path.size() - 1);
    }
}
