public class Solution {
    public boolean isSymmetric(TreeNode root) {
        return process(root.left, root.right);
    }

    public boolean process(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        // left != null && right != null
        return (left.val == right.val) && process(left.left, right.right) && process(left.right, right.left);
    }
}
