import java.util.*;

public class Solution_617 {
    // 深度优先搜索（递归）
    public TreeNode mergeTrees1(TreeNode root1, TreeNode root2) {
        if (root1 == null) return root2;
        if (root2 == null) return root1;
        root1.val += root2.val;
        root1.left = mergeTrees1(root1.left, root2.left);
        root1.right = mergeTrees1(root1.right, root2.right);
        return root1;
    }

    // 广度优先搜索（使用三个队列）
    public TreeNode mergeTrees2(TreeNode t1, TreeNode t2) {
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }
        TreeNode merged = new TreeNode(t1.val + t2.val);
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        Queue<TreeNode> queue1 = new LinkedList<TreeNode>();
        Queue<TreeNode> queue2 = new LinkedList<TreeNode>();
        queue.offer(merged);
        queue1.offer(t1);
        queue2.offer(t2);
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            TreeNode node = queue.poll(), node1 = queue1.poll(), node2 = queue2.poll();
            TreeNode left1 = node1.left, left2 = node2.left, right1 = node1.right, right2 = node2.right;
            if (left1 != null || left2 != null) {
                if (left1 != null && left2 != null) {
                    TreeNode left = new TreeNode(left1.val + left2.val);
                    node.left = left;
                    queue.offer(left);
                    queue1.offer(left1);
                    queue2.offer(left2);
                } else if (left1 != null) {
                    node.left = left1;
                } else if (left2 != null) {
                    node.left = left2;
                }
            }
            if (right1 != null || right2 != null) {
                if (right1 != null && right2 != null) {
                    TreeNode right = new TreeNode(right1.val + right2.val);
                    node.right = right;
                    queue.offer(right);
                    queue1.offer(right1);
                    queue2.offer(right2);
                } else if (right1 != null) {
                    node.right = right1;
                } else {
                    node.right = right2;
                }
            }
        }
        return merged;
    }

    public static void main(String[] args) {
        // Tree a
        TreeNode ta1 = new TreeNode(1);
        TreeNode ta2 = new TreeNode(3);
        TreeNode ta3 = new TreeNode(2);
        TreeNode ta4 = new TreeNode(5);
        ta1.left = ta2;
        ta1.right = ta3;
        ta2.left = ta4;
        // Tree b
        TreeNode tb1 = new TreeNode(2);
        TreeNode tb2 = new TreeNode(1);
        TreeNode tb3 = new TreeNode(3);
        TreeNode tb4 = new TreeNode(4);
        TreeNode tb5 = new TreeNode(7);
        tb1.left = tb2;
        tb1.right = tb3;
        tb2.right = tb4;
        tb3.right = tb5;

        Solution_617 s = new Solution_617();
        TreeNode res = s.mergeTrees2(ta1, tb1);
        // 打印合并后的树
        Deque<TreeNode> q = new LinkedList<>();
        q.add(res);
        while (!q.isEmpty()) {
            List<TreeNode> l = new LinkedList<>();
            while (!q.isEmpty()) {
                TreeNode tmp = q.poll();
                l.add(tmp);
            }
            for (TreeNode tmp : l) {
                System.out.print(tmp.val + " ");
                if (tmp.left != null) q.add(tmp.left);
                if (tmp.right != null) q.add(tmp.right);
            }
            System.out.println();
        }
    }
}
