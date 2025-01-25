package editor.cn;

import javax.swing.tree.TreeNode;

// 530.二叉搜索树的最小绝对差
class Solution530 {

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

        TreeNode pre;

        int minDiff = Integer.MAX_VALUE;

        public int getMinimumDifference(TreeNode root) {
            dfs(root);
            return minDiff;
        }

        public void dfs(TreeNode root) {
            if (root == null) {
                return;
            }

            // 中序遍历
            dfs(root.left);

            if (pre != null) {
                int curDiff = root.val - pre.val;
                minDiff = Math.min(curDiff, minDiff);
            }
            pre = root;

            dfs(root.right);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}