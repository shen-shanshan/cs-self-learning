/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null) {
            return false;
        }
        return compare(A, B) || isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }

    // 递归判断
    public boolean compare(TreeNode A, TreeNode B) {
        // base case
        // A 和 B 只要有一个为 null，就结束递归的过程
        if (A == null || B == null) {
            return A != null || B == null;
        }
        // A ！= null && B != null
        if (A.val != B.val) {
            return false;
        }
        // A.val == B.val
        return compare(A.left, B.left) && compare(A.right, B.right);
    }
}
