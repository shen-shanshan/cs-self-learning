package editor.cn;

import java.util.ArrayList;
import java.util.List;

// 590.N 叉树的后序遍历
class Solution590 {

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

        List<Integer> ans = new ArrayList<>();

        public List<Integer> postorder(Node root) {
            dfs(root);
            return ans;
        }

        public void dfs(Node root) {
            if (root == null) {
                return;
            }

            for (Node x : root.children) {
                dfs(x);
            }

            ans.add(root.val);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}