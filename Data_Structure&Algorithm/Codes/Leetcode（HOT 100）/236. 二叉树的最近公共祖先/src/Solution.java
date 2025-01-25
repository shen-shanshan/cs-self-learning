public class Solution {

    private TreeNode ans;

    public Solution() {
        this.ans = null;
    }

    public boolean dfs(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return false;
        // lson 表示左子树是否包含 p 或 q 节点
        boolean lson = dfs(root.left, p, q);
        // rson 表示右子树是否包含 p 或 q 节点
        boolean rson = dfs(root.right, p, q);
        // 当前节点就是最近公共祖先的情况：
        // 1. p、q 分别在左右子树
        // 2. 当前节点就是 p 或 q，且在其子树上包含另一个节点
        if ((lson && rson) || ((root.val == p.val || root.val == q.val) && (lson || rson))) {
            ans = root;
        }
        return lson || rson || (root.val == p.val || root.val == q.val);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        dfs(root, p, q);
        return ans;
    }
}