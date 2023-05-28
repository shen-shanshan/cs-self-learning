import java.util.*;

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

public class Solution_94 {

    // 中序遍历：左 ——> 根 ——> 右

    /* 递归：
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorder(root, res);
        return res;
    }

    public void inorder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }*/

    // 迭代：
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> st = new LinkedList<TreeNode>();
        TreeNode t = root;
        while (!st.isEmpty() || t != null) {
            while (t != null) {
                st.push(t);
                t = t.left;
            }
            t = st.pop();
            res.add(t.val);
            t = t.right;
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        TreeNode t4 = new TreeNode(4);
        t1.left = t2;
        t1.right = t3;
        t3.left = t4;

        Solution_94 s = new Solution_94();
        List<Integer> res = s.inorderTraversal(t1);
        for (Integer x : res) {
            System.out.print(x + " "); // 2, 1 ,4 ,3
        }
    }
}
