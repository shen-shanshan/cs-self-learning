package editor.cn;

import javax.swing.tree.TreeNode;

// 572.另一棵树的子树
class Solution572 {

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
        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            if (root == null) {
                return false;
            }

            if (isEqual(root, subRoot)) {
                return true;
            }

            return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
        }

        public boolean isEqual(TreeNode p, TreeNode q) {
            if (p == null && q == null) {
                return true;
            } else if (p == null || q == null) {
                return false;
            }

            if (p.val != q.val) {
                return false;
            }

            return isEqual(p.left, q.left) && isEqual(p.right, q.right);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}