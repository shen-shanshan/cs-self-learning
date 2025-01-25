/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 222.完全二叉树的节点个数
 */
public class Solution222 {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = getDepth(root.left);
        int right = getDepth(root.right);

        if (left == right) {
            // 左子树是满二叉树
            return (1 << left) + countNodes(root.right);
        } else {
            // 右子树是满二叉树
            return (1 << right) + countNodes(root.left);
        }
    }

    public int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int depth = 1;

        while (root.left != null) {
            depth++;
            root = root.left;
        }

        return depth;
    }
}
