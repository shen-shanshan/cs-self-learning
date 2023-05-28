package editor.cn;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 429.N 叉树的层序遍历
class Solution429 {

    //leetcode submit region begin(Prohibit modification and deletion)
/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
};
*/

    class Solution {
        public List<List<Integer>> levelOrder(Node root) {

            List<List<Integer>> ans = new LinkedList<>();

            if (root == null) {
                return ans;
            }

            Queue<Node> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> list = new LinkedList<>();
                while (size > 0) {
                    Node cur = queue.poll();
                    list.add(cur.val);
                    for (Node n : cur.children) {
                        if (n != null) {
                            queue.offer(n);
                        }
                    }
                    size--;
                }
                ans.add(list);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}