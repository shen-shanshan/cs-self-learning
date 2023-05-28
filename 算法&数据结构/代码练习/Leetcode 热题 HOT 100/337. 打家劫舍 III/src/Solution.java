import java.util.HashMap;
import java.util.Map;

/*动态规划：
* 我们可以用 f(o) 表示选择 o 节点的情况下，o 节点的子树上被选择的节点的最大权值和；
* g(o) 表示不选择 o 节点的情况下，o 节点的子树上被选择的节点的最大权值和；
* l 和 r 代表 o 的左右孩子。
* 当 o 被选中时，o 的左右孩子都不能被选中。
*     故 o 被选中情况下子树上被选中点的最大权值和为 l 和 r 不被选中的最大权值和相加。
*     即 f(o) = g(l) + g(r)。
* 当 o 不被选中时，o 的左右孩子可以被选中，也可以不被选中。
*     对于 o 的某个具体的孩子 x，它对 o 的贡献是 x 被选中和不被选中情况下权值和的较大值。
*     故 g(o) = max{f(l),g(l)} + max{f(r),g(r)}。
* */
public class Solution {
    Map<TreeNode, Integer> f = new HashMap<TreeNode, Integer>();
    Map<TreeNode, Integer> g = new HashMap<TreeNode, Integer>();

    public int rob(TreeNode root) {
        dfs(root);
        return Math.max(f.getOrDefault(root, 0), g.getOrDefault(root, 0));
    }

    // 后序遍历：左、右、头
    public void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        dfs(node.left);
        dfs(node.right);
        f.put(node, node.val + g.getOrDefault(node.left, 0) + g.getOrDefault(node.right, 0));
        g.put(node, Math.max(f.getOrDefault(node.left, 0), g.getOrDefault(node.left, 0)) + Math.max(f.getOrDefault(node.right, 0), g.getOrDefault(node.right, 0)));
    }
}