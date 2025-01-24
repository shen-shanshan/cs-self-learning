package editor.cn;

import javax.swing.tree.TreeNode;

// 98.验证二叉搜索树
class Solution98 {

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

        long last = Long.MIN_VALUE;

        public boolean isValidBST(TreeNode root) {
            if (root == null) {
                return true;
            }

            // 中序遍历
            boolean leftFlag = isValidBST(root.left);

            if (root.val > last) {
                last = (long) root.val;
            } else {
                return false;
            }

            boolean rightFlag = isValidBST(root.right);

            return leftFlag && rightFlag;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}