package editor.cn;

import javax.swing.tree.TreeNode;

// 654.最大二叉树
class Solution654 {

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
        public TreeNode constructMaximumBinaryTree(int[] nums) {
            if (nums == null || nums.length == 0) {
                return null;
            }

            return build(nums, 0, nums.length - 1);
        }

        public TreeNode build(int[] nums, int start, int end) {
            if (start > end) {
                return null;
            }

            if (start == end) {
                return new TreeNode(nums[start]);
            }

            // 找到最大值的下标
            int max = start;
            for (int i = start; i <= end; i++) {
                if (nums[i] > nums[max]) {
                    max = i;
                }
            }
            TreeNode root = new TreeNode(nums[max]);

            root.left = build(nums, start, max - 1);
            root.right = build(nums, max + 1, end);

            return root;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}