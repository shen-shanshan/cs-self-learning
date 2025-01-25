package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;

// 501.二叉搜索树中的众数
class Solution501 {

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

        TreeNode pre = null;

        int count = 0;

        int maxCount = 0;

        List<Integer> list = new LinkedList<>();

        public int[] findMode(TreeNode root) {
            dfs(root);

            int[] ans = new int[list.size()];

            for (int i = 0; i < list.size(); i++) {
                ans[i] = list.get(i);
            }

            return ans;
        }

        public void dfs(TreeNode root) {
            if (root == null) {
                return;
            }

            // 中序遍历
            dfs(root.left);

            if (pre == null || pre.val != root.val) {
                count = 1;
            } else {
                count++;
            }

            if (count > maxCount) {
                maxCount = count;
                list.clear();
                list.add(root.val);
            } else if (count == maxCount) {
                list.add(root.val);
            }

            pre = root;

            dfs(root.right);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}