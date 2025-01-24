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

public class Solution_653 {

    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> map = new HashSet<>();
        Deque<TreeNode> stack = new LinkedList<>();
        if (root == null) return false;
        // 前序遍历
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                if (map.contains(k - root.val)) return true;
                map.add(root.val);
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            root = root.right;
        }
        return false;
    }

    /* 官方：
    public boolean findTarget(TreeNode root, int k) {
        List < Integer > list = new ArrayList();
        inorder(root, list);
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int sum = list.get(l) + list.get(r);
            if (sum == k)
                return true;
            if (sum < k)
                l++;
            else
                r--;
        }
        return false;
    }
    public void inorder(TreeNode root, List < Integer > list) {
        if (root == null)
            return;
        inorder(root.left, list);
        list.add(root.val);
        inorder(root.right, list);
    }*/

    public static void main(String[] args) {
        /*TreeNode t1 = new TreeNode(4);
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
        t3.right = t7;*/
        //      4
        //   2    7
        // 1  3  6  9

        TreeNode t1 = new TreeNode(2);
        TreeNode t2 = new TreeNode(3);
        t1.right = t2;

        int target = 6;

        Solution_653 s = new Solution_653();
        boolean res = s.findTarget(t1, target);
        System.out.println(res);
    }
}
