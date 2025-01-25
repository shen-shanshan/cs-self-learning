import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    int count;

    int k;

    int ans;

    public int kthLargest(TreeNode root, int k) {
        this.k = k;
        count = 0;
        dfs(root);
        return ans;
    }

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        // 右 头 左
        dfs(root.right);
        count++;
        if (count == k) {
            ans = root.val;
            return;
        }
        dfs(root.left);
    }

}
