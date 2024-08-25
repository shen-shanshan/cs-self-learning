package editor.cn;

import javax.swing.tree.TreeNode;

// 222.完全二叉树的节点个数
class Solution222 {

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
        public int countNodes(TreeNode root) {
            if (root == null) {
                return 0;
            }

            int leftDepth = getDepth(root.left);
            int rightDepth = getDepth(root.right);

            if (leftDepth == rightDepth) {
                // 左子树是完全二叉树
                return (1 << leftDepth) + countNodes(root.right);
            } else {
                // 右子树是完全二叉树
                return (1 << rightDepth) + countNodes(root.left);
            }
        }

        public int getDepth(TreeNode root) {
            int count = 0;
            while (root != null) {
                root = root.left;
                count++;
            }
            return count;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}