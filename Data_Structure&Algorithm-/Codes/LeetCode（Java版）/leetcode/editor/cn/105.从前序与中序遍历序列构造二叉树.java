package editor.cn;

import javax.swing.tree.TreeNode;

// 105.从前序与中序遍历序列构造二叉树
class Solution105 {

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
        public TreeNode buildTree(int[] preorder, int[] inorder) {
            if (preorder == null || preorder.length == 0) {
                return null;
            }

            return process(preorder, 0, preorder.length - 1,
                    inorder, 0, inorder.length - 1);
        }

        public TreeNode process(int[] preorder, int preStart, int preEnd,
                                int[] inorder, int inStart, int inEnd) {
            if (preStart > preEnd) {
                return null;
            }

            if (preStart == preEnd) {
                return new TreeNode(preorder[preStart]);
            }

            // 找到头节点
            TreeNode root = new TreeNode(preorder[preStart]);
            int head = inStart;
            int leftLen = 0;
            while (inorder[head] != root.val) {
                head++;
                leftLen++;
            }

            root.left = process(preorder, preStart + 1, preStart + leftLen,
                    inorder, inStart, head - 1);
            root.right = process(preorder, preStart + leftLen + 1, preEnd,
                    inorder, head + 1, inEnd);

            return root;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}