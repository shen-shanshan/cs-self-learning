//Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class Solution_98 {

    /* 自己写的（有错）：
    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        boolean x1 = root.left == null || root.left.val < root.val;
        boolean x2 = root.right == null || root.right.val > root.val;
        return x1 && x2 && isValidBST(root.left) && isValidBST(root.right);
    }*/

    // 官方：
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean isValidBST(TreeNode node, long lower, long upper) {
        if (node == null) {
            return true;
        }
        // 判断子树的上限值与下限值
        if (node.val <= lower || node.val >= upper) {
            return false;
        }
        return isValidBST(node.left, lower, node.val) && isValidBST(node.right, node.val, upper);
    }

    public static void main(String[] args) {
        // 树 a （二叉搜索树）
        TreeNode t1 = new TreeNode(4);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(7);
        TreeNode t4 = new TreeNode(1);
        TreeNode t5 = new TreeNode(3);
        TreeNode t6 = new TreeNode(6);
        TreeNode t7 = new TreeNode(9);
        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        t3.left = t6;
        t3.right = t7;
        //      4
        //   2    7
        // 1  3  6  9

        // 树 b （不是搜索树）
        TreeNode n1 = new TreeNode(5);
        TreeNode n2 = new TreeNode(1);
        TreeNode n3 = new TreeNode(4);
        TreeNode n4 = new TreeNode(3);
        TreeNode n5 = new TreeNode(6);
        n1.left = n2;
        n1.right = n3;
        n3.left = n4;
        n3.right = n5;

        Solution_98 s = new Solution_98();
        boolean res = s.isValidBST(n1);
        System.out.println(res);
    }
}
