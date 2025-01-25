package editor.cn;

import javax.swing.tree.TreeNode;

// 226.翻转二叉树
class Solution226 {

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
        public TreeNode invertTree(TreeNode root) {

            if (root == null) {
                return null;
            }

            TreeNode left = root.left;
            root.left = invertTree(root.right);
            root.right = invertTree(left);

            return root;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}