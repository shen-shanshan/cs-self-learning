package editor.cn;

import javax.swing.tree.TreeNode;

// 106.从中序与后序遍历序列构造二叉树
class Solution106 {

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
        public TreeNode buildTree(int[] inorder, int[] postorder) {
            if (inorder == null || inorder.length == 0) {
                return null;
            }

            return process(inorder, 0, inorder.length - 1,
                    postorder, 0, postorder.length - 1);
        }

        public TreeNode process(int[] inorder, int inStart, int inEnd,
                                int[] postorder, int postStart, int postEnd) {
            if (inStart > inEnd) {
                return null;
            }

            if (inEnd == inStart) {
                return new TreeNode(inorder[inStart]);
            }

            // 头节点
            TreeNode root = new TreeNode(postorder[postEnd]);
            int head = inStart;
            int leftLen = 0;
            while (inorder[head] != postorder[postEnd]) {
                head++;
                leftLen++;
            }

            root.left = process(inorder, inStart, head - 1,
                    postorder, postStart, postStart + leftLen - 1);
            root.right = process(inorder, head + 1, inEnd,
                    postorder, postStart + leftLen, postEnd - 1);

            return root;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}