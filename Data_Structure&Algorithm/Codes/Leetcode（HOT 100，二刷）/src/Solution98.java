/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 98.验证二叉搜索树
 */
public class Solution98 {

    long last = Long.MIN_VALUE;

    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }

        // 中序遍历
        boolean leftFlag = isValidBST(root.left);

        if (root.val > last) {
            last = (long) root.val;
        } else {
            return false;
        }

        boolean rightFlag = isValidBST(root.right);

        return leftFlag && rightFlag;
    }
}
