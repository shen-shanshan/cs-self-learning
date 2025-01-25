package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;

// 113.路径总和 II
class Solution113 {

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

        List<List<Integer>> ans = new LinkedList<>();
        List<Integer> path = new LinkedList<>();

        public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
            if (root == null) {
                return ans;
            }

            path.add(root.val);
            backtrack(root, targetSum - root.val);

            return ans;
        }

        public void backtrack(TreeNode root, int targetSum) {
            // 叶子节点
            if (root.left == null && root.right == null && targetSum == 0) {
                ans.add(new LinkedList<>(path));
            }

            // 递归左子树
            if (root.left != null) {
                path.add(root.left.val);
                backtrack(root.left, targetSum - root.left.val);
                path.remove(path.size() - 1);
            }
            // 递归右子树
            if (root.right != null) {
                path.add(root.right.val);
                backtrack(root.right, targetSum - root.right.val);
                path.remove(path.size() - 1);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}