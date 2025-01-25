package editor.cn;

import javax.swing.tree.TreeNode;

// 337.打家劫舍 III
class Solution337 {

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
        public int rob(TreeNode root) {
            int[] ans = dfs(root);
            return Math.max(ans[0], ans[1]);
        }

        public int[] dfs(TreeNode root) {
            if (root == null) {
                return new int[]{0, 0};
            }

            // 后序遍历
            int[] left = dfs(root.left);
            int[] right = dfs(root.right);

            // 不拿当前节点：此时可以拿左右子节点，也可以不拿，取较大的情况
            int val1 = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
            // 拿当前节点：此时不能再拿左右子节点
            int val2 = left[0] + right[0] + root.val;

            return new int[]{val1, val2};
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}