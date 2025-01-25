/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 98.验证二叉搜索树
 */
public class Solution98 {
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }

        return dfs(root).isValid;
    }

    public Info dfs(TreeNode root) {
        if (root == null) {
            return null;
        }

        Info left = dfs(root.left);
        Info right = dfs(root.right);

        if (left == null && right == null) {
            return new Info(root.val, root.val, true);
        }

        int min = root.val;
        int max = root.val;
        boolean isValid = true;

        if (left != null) {
            if (!left.isValid || left.max >= root.val) {
                isValid = false;
            }
            min = Math.min(left.min, min);
            max = Math.max(left.max, max);
        }
        if (right != null) {
            if (!right.isValid || right.min <= root.val) {
                isValid = false;
            }
            min = Math.min(right.min, min);
            max = Math.max(right.max, max);
        }

        return new Info(min, max, isValid);
    }

    class Info {

        int min;

        int max;

        boolean isValid;

        public Info(int min, int max, boolean isValid) {
            this.min = min;
            this.max = max;
            this.isValid = isValid;
        }
    }
}
