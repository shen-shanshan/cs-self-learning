/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 450.删除二叉搜索树中的节点
 */
public class Solution450 {
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (root.val < key) {
            root.right = deleteNode(root.right, key);
        } else {
            // root.val == key
            if (root.left == null && root.right == null) {
                return null;
            } else if (root.left != null && root.right == null) {
                return root.left;
            }
            // right != null
            TreeNode left = root.left;
            root = root.right;
            TreeNode min = root;
            while (min.left != null) {
                min = min.left;
            }
            min.left = left;
        }

        return root;
    }
}
