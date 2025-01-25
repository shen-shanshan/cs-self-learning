package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 515.在每个树行中找最大值
class Solution515 {

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
        public List<Integer> largestValues(TreeNode root) {

            List<Integer> ans = new LinkedList<>();

            if (root == null) {
                return ans;
            }

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                int max = Integer.MIN_VALUE;
                while (size > 0) {
                    TreeNode cur = queue.poll();
                    max = Math.max(max, cur.val);
                    size--;
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                ans.add(max);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}