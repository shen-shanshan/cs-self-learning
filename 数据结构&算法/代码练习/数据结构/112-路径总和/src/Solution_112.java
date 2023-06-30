import java.util.Deque;
import java.util.LinkedList;
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

public class Solution_112 {

    /* 递归（深度优先搜索）：
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        // 若 root 是叶子节点
        if ((root.left == null && root.right == null) && root.val == targetSum) return true;
        targetSum -= root.val;
        return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
    }*/

    /* 广度优先搜索：
    用广度优先搜索的方式，记录从根节点到当前节点的路径和，以防止重复计算。
    用两个队列，分别存储将要遍历的节点，以及根节点到这些节点的路径和即可。*/
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        // 存储被遍历到的节点
        Queue<TreeNode> queNode = new LinkedList<TreeNode>();
        // 存储被遍历到的节点的路径上的值的和
        Queue<Integer> queVal = new LinkedList<Integer>();
        queNode.offer(root);
        queVal.offer(root.val);
        while (!queNode.isEmpty()) {
            TreeNode now = queNode.poll();
            int temp = queVal.poll();
            // 判断是否为叶子节点
            if (now.left == null && now.right == null) {
                if (temp == targetSum) {
                    return true;
                }
                continue;
            }
            if (now.left != null) {
                queNode.offer(now.left);
                queVal.offer(now.left.val + temp);
            }
            if (now.right != null) {
                queNode.offer(now.right);
                queVal.offer(now.right.val + temp);
            }
        }
        return false;
    }

    public static void main(String[] args) {
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
        int target = 87;

        Solution_112 s = new Solution_112();
        boolean res = s.hasPathSum(t1, target);
        System.out.println(res);
    }
}
