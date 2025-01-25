/*优化：
 * 我们可以设计一个结构，表示某个节点的 f 和 g 值。
 * 在每次递归返回的时候，都把这个点对应的 f 和 g 返回给上一级调用，这样可以省去哈希表的空间。
 * */
public class Solution2 {
    public int rob(TreeNode root) {
        int[] rootStatus = dfs(root);
        return Math.max(rootStatus[0], rootStatus[1]);
    }

    public int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] l = dfs(node.left);
        int[] r = dfs(node.right);
        int selected = node.val + l[1] + r[1];
        int notSelected = Math.max(l[0], l[1]) + Math.max(r[0], r[1]);
        return new int[]{selected, notSelected};
    }
}