/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    TreeNode ans;

    boolean flag = true;

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Info info = dfs(root, p, q);
        return ans;
    }

    public Info dfs(TreeNode root, TreeNode p, TreeNode q) {
        // base case
        if (root == null) {
            return new Info(0, false, false);
        }
        // 向下递归，收集信息
        Info leftInfo = dfs(root.left, p, q);
        Info rightInfo = dfs(root.right, p, q);
        // 生成本层信息
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean containsP = leftInfo.containsP || rightInfo.containsP || root.val == p.val;
        boolean containsQ = leftInfo.containsQ || rightInfo.containsQ || root.val == q.val;
        if (flag && containsP && containsQ) {
            ans = root;
            flag = false;
        }
        return new Info(height, containsP, containsQ);
    }

}

class Info {
    int height;
    boolean containsP;
    boolean containsQ;

    public Info(int height, boolean containsP, boolean containsQ) {
        this.height = height;
        this.containsP = containsP;
        this.containsQ = containsQ;
    }
}
