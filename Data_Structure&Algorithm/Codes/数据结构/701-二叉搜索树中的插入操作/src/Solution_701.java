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

public class Solution_701 {

    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        TreeNode tmp = root;
        int flag = 0;
        TreeNode nowRoot = new TreeNode();
        while (tmp != null) {
            nowRoot = tmp;
            flag = 0;
            if (val < tmp.val) {
                tmp = tmp.left;
                flag = 1;
            } else {
                tmp = tmp.right;
                flag = 2;
            }
        }
        if (flag == 1) {
            nowRoot.left = new TreeNode(val);
        } else if (flag == 2) {
            nowRoot.right = new TreeNode(val);
        }
        return root;
    }

    /* 官方：
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        TreeNode pos = root;
        while (pos != null) {
            if (val < pos.val) {
                if (pos.left == null) {
                    pos.left = new TreeNode(val);
                    break;
                } else {
                    pos = pos.left;
                }
            } else {
                if (pos.right == null) {
                    pos.right = new TreeNode(val);
                    break;
                } else {
                    pos = pos.right;
                }
            }
        }
        return root;
    }*/

    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(4);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(7);
        TreeNode t4 = new TreeNode(1);
        TreeNode t5 = new TreeNode(3);
        // TreeNode t6 = new TreeNode(6);
        // TreeNode t7 = new TreeNode(9);
        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        // t3.left = t6;
        // t3.right = t7;
        //      4
        //   2    7
        // 1  3

        Solution_701 s = new Solution_701();
        TreeNode res = s.insertIntoBST(t1, 5);
        // 层序遍历
        List<List<Integer>> l = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(res);
        while (!q.isEmpty()) {
            int len = q.size();
            List<Integer> arr = new ArrayList<>();
            while (len > 0) {
                TreeNode tmp = q.poll();
                arr.add(tmp.val);
                len--;
                if (tmp.left != null) {
                    q.add(tmp.left);
                }
                if (tmp.right != null) {
                    q.add(tmp.right);
                }
            }
            l.add(arr);
        }
        // 打印
        for (List<Integer> x : l) {
            for (Integer y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}
