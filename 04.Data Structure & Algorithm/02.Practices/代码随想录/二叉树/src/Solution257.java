import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 257.二叉树的所有路径
 */
public class Solution257 {

    List<String> ans = new ArrayList<>();

    List<Integer> path = new ArrayList<>();

    public List<String> binaryTreePaths(TreeNode root) {
        if (root == null) {
            return ans;
        }

        backtrack(root);

        return ans;
    }

    public void backtrack(TreeNode root) {
        // 终止条件：叶子节点
        if (root.left == null && root.right == null) {
            StringBuilder sb = new StringBuilder();
            for (int x : path) {
                sb.append(x + "->");
            }
            sb.append(root.val);
            ans.add(sb.toString());
        }

        path.add(root.val);

        if (root.left != null) {
            backtrack(root.left);
        }
        if (root.right != null) {
            backtrack(root.right);
        }

        // 回溯
        path.remove(path.size() - 1);
    }
}
