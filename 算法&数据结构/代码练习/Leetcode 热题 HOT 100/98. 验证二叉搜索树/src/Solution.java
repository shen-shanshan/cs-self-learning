public class Solution {
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        return recursion(root).isBST;
    }

    public Info recursion(TreeNode root) {
        // base case
        if (root == null) {
            return null;
        }
        int min = root.val;
        int max = root.val;
        boolean isBST = true;
        // 向下递归，并收集信息
        Info left = recursion(root.left);
        Info right = recursion(root.right);
        // 更新最小值
        if (left != null) {
            min = Math.min(min, left.min);
        }
        if (right != null) {
            min = Math.min(min, right.min);
        }
        // 更新最大值
        if (left != null) {
            max = Math.max(max, left.max);
        }
        if (right != null) {
            max = Math.max(max, right.max);
        }
        // 判断当前子树是否为二叉搜索树
        if (left != null && !left.isBST) {
            isBST = false;
        }
        if (right != null && !right.isBST) {
            isBST = false;
        }
        if (left != null && left.max >= root.val) {
            isBST = false;
        }
        if (right != null && right.min <= root.val) {
            isBST = false;
        }
        // 返回信息
        return new Info(isBST, min, max);
    }
}

class Info {
    boolean isBST;
    int min;
    int max;

    public Info(boolean isBST, int min, int max) {
        this.isBST = isBST;
        this.min = min;
        this.max = max;
    }
}