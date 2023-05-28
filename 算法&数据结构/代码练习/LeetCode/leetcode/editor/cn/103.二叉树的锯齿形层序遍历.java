package editor.cn;

import javax.swing.tree.TreeNode;
import java.util.*;

// 103.二叉树的锯齿形层序遍历
class Solution103 {

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
        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            List<List<Integer>> ans = new ArrayList<>();

            if (root == null) {
                return ans;
            }

            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);

            boolean flag = true;

            while (!stack.isEmpty()) {
                List<Integer> list = new ArrayList<>();

                int len = stack.size();
                Stack<TreeNode> nextLayer = new Stack<>();

                while (len > 0) {
                    TreeNode cur = stack.pop();
                    list.add(cur.val);

                    if (flag) {
                        // 先加入左节点，再加入右节点
                        if (cur.left != null) {
                            nextLayer.push(cur.left);
                        }
                        if (cur.right != null) {
                            nextLayer.push(cur.right);
                        }
                    } else {
                        // 先加入右节点，再加入左节点
                        if (cur.right != null) {
                            nextLayer.push(cur.right);
                        }
                        if (cur.left != null) {
                            nextLayer.push(cur.left);
                        }
                    }

                    len--;
                }

                ans.add(list);
                flag = !flag;
                stack = nextLayer;
            }

            return ans;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}