package editor.cn;

import javax.swing.tree.TreeNode;

// 101.对称二叉树
class Solution101 {

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
        public boolean isSymmetric(TreeNode root) {
            if (root == null) {
                return true;
            }

            return myIsSymmetric(root.left, root.right);
        }

        public boolean myIsSymmetric(TreeNode left, TreeNode right) {
            if (left == null && right == null) {
                return true;
            } else if (left == null || right == null) {
                return false;
            }

            if (left.val != right.val) {
                return false;
            }

            return myIsSymmetric(left.left, right.right) && myIsSymmetric(left.right, right.left);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}