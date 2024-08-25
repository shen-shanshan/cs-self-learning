package editor.cn;

import java.util.ArrayList;
import java.util.List;

// 589.N 叉树的前序遍历
class Solution589 {

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

        public List<Integer> preorder(Node root) {
            dfs(root);
            return ans;
        }

        public void dfs(Node root) {
            if (root == null) {
                return;
            }

            // 头、左、右
            ans.add(root.val);
            for (Node child : root.children) {
                dfs(child);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}