public class Solution {
    public void flatten(TreeNode root) {
        // base case
        if (root == null) {
            return;
        } else if (root.left == null && root.right == null) {
            return;
        }
        // 记录左、右子节点
        TreeNode left = root.left;
        TreeNode right = root.right;
        // 向下递归
        flatten(left);
        flatten(right);
        // 将当前节点的左树置空
        root.left = null;
        // 连接链表
        root.right = left;
        // 获取左子树的末尾节点
        while (root.right != null) {
            root = root.right;
        }
        root.right = right;
    }
}