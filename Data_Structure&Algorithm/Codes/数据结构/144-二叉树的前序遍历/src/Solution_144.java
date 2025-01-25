import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

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

public class Solution_144 {

    // 前序遍历：根 ——> 左 ——> 右

    /* 递归：
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preorder(root, res);
        return res;
    }

    public void preorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preorder(root.left, res);
        preorder(root.right, res);
    }*/

    // 迭代：
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) {
            return res;
        }
        /* Deque（双端队列）：
        1.Deque 是一个双端队列接口，继承自 Queue 接口。
          Deque 的实现类是 LinkedList、ArrayDeque、LinkedBlockingDeque，其中 LinkedList 是最常用的。
        2.三种用法：
          （1）普通队列（一端进另一端出）：Queue queue = new LinkedList()
            【1】Deque接口扩展（继承）了 Queue 接口。
            【2】在将双端队列用作队列时，将得到 FIFO（先进先出）行为。
            【3】将元素添加到双端队列的末尾，从双端队列的开头移除元素。从 Queue 接口继承的方法完全等效于 Deque 方法。
          （2）双端队列（两端都可进出）：Deque deque = new LinkedList()
          （3）代替栈（stack）：Deque deque = new LinkedList()
            【1】Java堆栈 Stack 类已经过时，Java官方推荐使用 Deque 替代 Stack 使用。
            【2】Deque堆栈操作方法：push()、pop()、peek()。
            【3】在将双端队列用作堆栈时，元素被推入双端队列的开头并从双端队列开头弹出。堆栈方法完全等效于 Deque 方法。
        3.Deque是一个线性collection，支持在两端插入和移除元素。
        名称 deque 是“double ended queue（双端队列）”的缩写，通常读为“deck”。
        大多数 Deque 实现对于它们能够包含的元素数没有固定限制，但此接口既支持有容量限制的双端队列，也支持没有固定大小限制的双端队列。
        */
        Deque<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode node = root;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                res.add(node.val);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        t1.right = t2;
        t2.left = t3;

        Solution_144 s = new Solution_144();
        List<Integer> res = s.preorderTraversal(t1);
        for (Integer x : res) {
            System.out.print(x + " ");
        }
    }
}
