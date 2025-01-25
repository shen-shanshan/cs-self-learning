/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 105.从前序与中序遍历序列构造二叉树
 */
public class Solution105 {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return myBuild(preorder, 0, preorder.length - 1,
                inorder, 0, inorder.length - 1);
    }

    public TreeNode myBuild(int[] pre, int preStart, int preEnd,
                            int[] in, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }

        if (preStart == preEnd) {
            return new TreeNode(pre[preEnd]);
        }

        // 头节点
        int head = pre[preStart];
        TreeNode root = new TreeNode(head);

        // 分割左右子树
        int i = inStart;
        while (in[i] != head) {
            i++;
        }
        int leftNum = i - inStart;

        root.left = myBuild(pre, preStart + 1, preStart + leftNum,
                in, inStart, i - 1);
        root.right = myBuild(pre, preStart + leftNum + 1, preEnd,
                in, i + 1, inEnd);

        return root;
    }
}
