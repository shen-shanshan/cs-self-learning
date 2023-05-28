package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;

// 257.二叉树的所有路径
class Solution257 {

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
        List<String> ans = new LinkedList<>();

        List<Integer> path = new LinkedList<>();

        public List<String> binaryTreePaths(TreeNode root) {
            if (root == null) {
                return ans;
            }
            backtrack(root);
            return ans;
        }

        public void backtrack(TreeNode root) {
            // 叶子节点
            if (root.left == null && root.right == null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < path.size(); i++) {
                    sb.append(path.get(i) + "->");
                }
                sb.append(root.val);
                ans.add(sb.toString());
            }

            path.add(root.val);

            if (root.left != null) {
                backtrack(root.left);
            }
            if (root.right != null) {
                backtrack(root.right);
            }

            // 回溯
            path.remove(path.size() - 1);
        }
    }

    //leetcode submit region end(Prohibit modification and deletion)

}