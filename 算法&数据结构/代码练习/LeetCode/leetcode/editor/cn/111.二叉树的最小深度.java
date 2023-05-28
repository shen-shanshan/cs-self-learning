package editor.cn;

import javax.swing.tree.TreeNode;

// 111.二叉树的最小深度
class Solution111 {

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
        public int minDepth(TreeNode root) {

            if (root == null) {
                return 0;
            }

            if (root.left == null && root.right == null) {
                return 1;
            }

            int Depth = Integer.MAX_VALUE;

            if (root.left != null) {
                Depth = Math.min(Depth, minDepth(root.left));
            }
            if (root.right != null) {
                Depth = Math.min(Depth, minDepth(root.right));
            }

            return Depth + 1;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}