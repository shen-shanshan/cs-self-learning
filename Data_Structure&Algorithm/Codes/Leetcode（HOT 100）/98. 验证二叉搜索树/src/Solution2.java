public class Solution2 {
    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        boolean x1 = root.left == null || root.left.val < root.val;
        boolean x2 = root.right == null || root.right.val > root.val;
        return x1 && x2 && isValidBST(root.left) && isValidBST(root.right);
    }
}
