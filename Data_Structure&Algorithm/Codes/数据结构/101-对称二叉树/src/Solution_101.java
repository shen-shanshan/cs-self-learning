import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

public class Solution_101 {

    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;

        // 递归：
        // 当根节点不为空时，判断左右两子树是否对称
        // return isSymmetricDouble(root.left, root.right);

        // 迭代：
        return check(root, root);
    }

    /* 定义一个新函数，用于判断两树是否对称
    public boolean isSymmetricDouble(TreeNode rootLeft, TreeNode rootRight) {
        if (rootLeft == null && rootRight == null) return true;
        if (rootLeft == null || rootRight == null) return false;
        boolean x1 = rootLeft.val == rootRight.val;
        boolean x2 = isSymmetricDouble(rootLeft.left, rootRight.right);
        boolean x3 = isSymmetricDouble(rootLeft.right, rootRight.left);
        return x1 && x2 && x3;
    }*/

    /*首先我们引入一个队列，这是把递归程序改写成迭代程序的常用方法。
    初始化时我们把根节点入队两次。
    每次提取两个结点并比较它们的值（队列中每两个连续的结点应该是相等的，而且它们的子树互为镜像）。
    然后将两个结点的左右子结点按相反的顺序插入队列中。
    当队列为空时，或者我们检测到树不对称（即从队列中取出两个不相等的连续结点）时，该算法结束。*/
    public boolean check(TreeNode u, TreeNode v) {
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        q.offer(u);
        q.offer(v);
        while (!q.isEmpty()) {
            u = q.poll();
            v = q.poll();
            if (u == null && v == null) {
                continue;
            }
            if ((u == null || v == null) || (u.val != v.val)) {
                return false;
            }

            q.offer(u.left);
            q.offer(v.right);

            q.offer(u.right);
            q.offer(v.left);
        }
        return true;
    }

    public static void main(String[] args) {
        // 二叉树 a (true)
        TreeNode ta1 = new TreeNode(1);
        TreeNode ta2 = new TreeNode(2);
        TreeNode ta3 = new TreeNode(2);
        TreeNode ta4 = new TreeNode(3);
        TreeNode ta5 = new TreeNode(4);
        TreeNode ta6 = new TreeNode(4);
        TreeNode ta7 = new TreeNode(3);
        ta1.left = ta2;
        ta1.right = ta3;
        ta2.left = ta4;
        ta2.right = ta5;
        ta3.left = ta6;
        ta3.right = ta7;

        // 二叉树 b (false)
        TreeNode tb1 = new TreeNode(1);
        TreeNode tb2 = new TreeNode(2);
        TreeNode tb3 = new TreeNode(2);
        TreeNode tb4 = new TreeNode(3);
        TreeNode tb5 = new TreeNode(3);
        tb1.left = tb2;
        tb1.right = tb3;
        tb2.right = tb4;
        tb3.right = tb5;

        Solution_101 s = new Solution_101();
        boolean res = s.isSymmetric(tb1);
        System.out.println(res);
    }
}
