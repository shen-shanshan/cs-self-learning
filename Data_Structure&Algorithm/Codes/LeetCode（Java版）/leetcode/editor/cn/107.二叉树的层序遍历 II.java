package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

// 107.二叉树的层序遍历 II
class Solution107 {

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
        public List<List<Integer>> levelOrderBottom(TreeNode root) {

            List<List<Integer>> ans = new LinkedList<>();
            Stack<List<Integer>> stack = new Stack<>();
            Queue<TreeNode> queue = new LinkedList<>();

            if (root == null) {
                return ans;
            }

            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> list = new LinkedList<>();
                while (size > 0) {
                    TreeNode cur = queue.poll();
                    list.add(cur.val);
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                    size--;
                }
                stack.add(list);
            }

            while (!stack.isEmpty()) {
                ans.add(stack.pop());
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}