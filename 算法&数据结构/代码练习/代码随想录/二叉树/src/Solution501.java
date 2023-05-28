import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 501.二叉搜索树中的众数
 */
public class Solution501 {
    public int[] findMode(TreeNode root) {
        if (root == null) {
            return new int[]{};
        }

        dfs(root);

        int[] arr = new int[ans.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ans.get(i);
        }

        return arr;
    }

    TreeNode pre = null;

    List<Integer> ans = new ArrayList<>();

    int count = 0;

    int max = 0;

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }

        dfs(root.left);

        if (pre == null || pre.val != root.val) {
            count = 1;
        } else {
            count++;
        }

        if (count > max) {
            max = count;
            ans.clear();
            ans.add(root.val);
        } else if (count == max) {
            ans.add(root.val);
        }

        pre = root;

        dfs(root.right);
    }
}
