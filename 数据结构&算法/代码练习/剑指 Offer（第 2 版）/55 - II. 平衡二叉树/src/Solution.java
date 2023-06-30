/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public boolean isBalanced(TreeNode root) {
        return process(root).isBalanced;
    }

    public Info process(TreeNode root) {
        if (root == null) {
            return new Info(0, true);
        }
        // 收集信息
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        // 生成信息
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced
                && (Math.abs(leftInfo.height - rightInfo.height) <= 1);
        return new Info(height, isBalanced);
    }

}

class Info {
    int height;
    boolean isBalanced;

    public Info(int height, boolean isBalanced) {
        this.height = height;
        this.isBalanced = isBalanced;
    }
}