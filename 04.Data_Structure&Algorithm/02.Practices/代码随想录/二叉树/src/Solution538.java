/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 538.把二叉搜索树转换为累加树
 */
public class Solution538 {

    int sum = 0;

    public TreeNode convertBST(TreeNode root) {
        dfs(root);
        return root;
    }

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }

        // 右
        dfs(root.right);

        // 头
        int cur = root.val;
        root.val += sum;
        sum += cur;

        // 左
        dfs(root.left);
    }
}
