public class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int len = preorder.length;
        return process(preorder, 0, inorder, 0, len);
    }

    public TreeNode process(int[] preorder, int preStart
            , int[] inorder, int inStart, int len) {
        // base case
        if (len == 0) {
            return null;
        }else if(len == 1){
            return new TreeNode(preorder[preStart]);
        }
        // 找头节点
        int i = inStart;
        while (inorder[i] != preorder[preStart]) {
            i++;
        }
        TreeNode head = new TreeNode(inorder[i]);
        // 左、右子树的大小
        int leftLen = i - inStart;
        int rightLen = len - leftLen - 1;
        // 递归建树
        head.left = process(preorder, preStart + 1
                , inorder, inStart, leftLen);
        head.right = process(preorder, preStart + leftLen + 1
                , inorder, i + 1, rightLen);
        return head;
    }
}