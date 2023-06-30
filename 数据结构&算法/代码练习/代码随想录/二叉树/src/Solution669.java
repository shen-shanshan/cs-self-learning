/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 669.修剪二叉搜索树
 */
public class Solution669 {
    public TreeNode trimBST(TreeNode root, int low, int high) {
        if (root == null) {
            return null;
        }

        if (root.val == low) {
            // 剪掉左子树
            root.left = null;
            root.right = trimBST(root.right, low, high);
        } else if (root.val == high) {
            // 剪掉右子树
            root.right = null;
            root.left = trimBST(root.left, low, high);
        }

        if (root.val < low) {
            // 只留右子树
            return trimBST(root.right, low, high);
        } else if (root.val > high) {
            // 只留左子树
            return trimBST(root.left, low, high);
        } else {
            // low < root.val < high
            root.left = trimBST(root.left, low, high);
            root.right = trimBST(root.right, low, high);
        }

        return root;
    }
}
