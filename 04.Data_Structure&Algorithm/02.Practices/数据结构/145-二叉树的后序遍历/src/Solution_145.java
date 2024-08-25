import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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

public class Solution_145 {

    // 中序遍历：左 ——> 右 ——> 根

    /* 递归：
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postorder(root, res);
        return res;
    }

    public void postorder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        postorder(root.left, res);
        postorder(root.right, res);
        res.add(root.val);
    }*/

    // 迭代：
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) return res;
        Deque<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode prev = null;
        while (root != null || !stack.isEmpty()) {
            // 先遍历到最左边的节点，并将元素依次压栈
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            // 只有当 root 左右都为空了，才把 root 加入返回数组
            if (root.right == null || root.right == prev) {
                res.add(root.val);
                // prev 用来记录已经加入返回数组的节点
                prev = root;
                // 继续弹栈出上一个元素
                root = null;
            } else {
                // 只要 root 的右边还有节点，就需要先把 root 压栈，在后面才弹出并加入返回数组
                stack.push(root);
                root = root.right;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(5);
        t1.left = t2;
        t1.right = t3;
        t3.left = t4;
        t3.right = t5;

        Solution_145 s = new Solution_145();
        List<Integer> res = s.postorderTraversal(t1);
        for (Integer x : res) {
            System.out.print(x + " "); // 2, 4, 5, 3, 1
        }
    }
}
