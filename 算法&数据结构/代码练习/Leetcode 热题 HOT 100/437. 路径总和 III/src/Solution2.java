import java.util.HashMap;
import java.util.Map;

/*
 * 前缀和：
 * 我们定义节点的前缀和为：由根结点到当前结点的路径上所有节点的和。
 * 假设当前从根节点 root 到节点 node 的前缀和为 curr。
 * 则此时我们在已保存的前缀和查找是否存在前缀和刚好等于 curr−targetSum。
 * 假设从根节点 root 到节点 node 的路径中存在节点 pi 到根节点 root 的前缀和为 curr−targetSum。
 * 则节点 pi+1 到 node 的路径上所有节点的和一定为 targetSum。
 * */
public class Solution2 {
    public int pathSum(TreeNode root, int targetSum) {
        // HashMap: <前缀和，该前缀和出现的次数>
        Map<Long, Integer> prefix = new HashMap<Long, Integer>();
        prefix.put(0L, 1);
        return dfs(root, prefix, 0, targetSum);
    }

    public int dfs(TreeNode root, Map<Long, Integer> prefix, long curr, int targetSum) {
        if (root == null) {
            return 0;
        }

        int ret = 0;
        curr += root.val;

        // 以当前节点为路径的结尾进行统计
        ret = prefix.getOrDefault(curr - targetSum, 0);
        // 将从根节点到当前节点的路径和加入前缀集合
        // 因为路径中可能会因为正负数抵消而出现重复的前缀，因此应先从 prefix 中取 curr 的值再加 1
        prefix.put(curr, prefix.getOrDefault(curr, 0) + 1);
        // 向下递归
        ret += dfs(root.left, prefix, curr, targetSum);
        ret += dfs(root.right, prefix, curr, targetSum);
        // 回溯
        prefix.put(curr, prefix.getOrDefault(curr, 0) - 1);

        return ret;
    }
}