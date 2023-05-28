package editor.cn;

import java.util.LinkedList;
import java.util.Queue;

// 116.填充每个节点的下一个右侧节点指针
class Solution116 {

    //leetcode submit region begin(Prohibit modification and deletion)
/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

    class Solution {
        public Node connect(Node root) {

            if (root == null) {
                return root;
            }

            Queue<Node> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                while (size > 0) {
                    Node cur = queue.poll();
                    if (size > 1) {
                        cur.next = queue.peek();
                    } else {
                        cur.next = null;
                    }
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                    size--;
                }
            }

            return root;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}