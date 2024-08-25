/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 101.对称二叉树
 */
public class Solution101 {
    public boolean isSymmetric(TreeNode root) {
        return solution(root.left, root.right);
    }

    public boolean solution(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }

        if (left.val != right.val) {
            return false;
        }

        return solution(left.left, right.right) && solution(left.right, right.left);
    }
}
