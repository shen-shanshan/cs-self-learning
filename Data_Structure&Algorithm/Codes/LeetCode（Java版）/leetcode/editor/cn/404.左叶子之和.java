package editor.cn;

import javax.swing.tree.TreeNode;

// 404.左叶子之和
class Solution404 {

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    class Solution {
        public int sumOfLeftLeaves(TreeNode root) {
            if (root == null) {
                return 0;
            }

            int leftSum = 0;
            int rightSum = 0;

            // 左叶子节点
            if (root.left != null && root.left.left == null && root.left.right == null) {
                leftSum = root.left.val;
            } else {
                leftSum = sumOfLeftLeaves(root.left);
            }

            rightSum = sumOfLeftLeaves(root.right);

            return leftSum + rightSum;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}