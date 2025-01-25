/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 106.从中序与后序遍历序列构造二叉树
 */
public class Solution106 {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        return myBuild(inorder, 0, inorder.length - 1,
                postorder, 0, postorder.length - 1);
    }

    public TreeNode myBuild(int[] in, int inStart, int inEnd,
                            int[] post, int postStart, int postEnd) {
        if (inStart > inEnd) {
            return null;
        }

        // 该子树只剩一个节点
        if (inStart == inEnd) {
            return new TreeNode(in[inStart]);
        }

        // 找到并创建头节点
        int head = post[postEnd];
        TreeNode root = new TreeNode(head);

        // 分割左右子树
        int i = inStart;
        while (in[i] != head) {
            i++;
        }
        int leftNum = i - inStart;

        root.left = myBuild(in, inStart, i - 1,
                post, postStart, postStart + leftNum - 1);
        root.right = myBuild(in, i + 1, inEnd,
                post, postStart + leftNum, postEnd - 1);

        return root;
    }
}
