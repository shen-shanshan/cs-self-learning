/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 968.监控二叉树
 * 总结：
 * 把摄像头放在叶子节点的父节点位置，才能充分利用摄像头的覆盖面积。
 * 头结点放不放摄像头也就省下一个摄像头， 叶子节点放不放摄像头省下了的摄像头数量是指数阶别的。
 * 自底向上推导：后序遍历
 * 节点状态：
 * 0：该节点无覆盖
 * 1：该节点有摄像头
 * 2：该节点有覆盖（无摄像头）
 * 空节点的状态只能是有覆盖：
 * 空节点不能是无覆盖的状态，这样叶子节点就要放摄像头了。
 * 空节点也不能是有摄像头的状态，这样叶子节点的父节点就没有必要放摄像头了。
 */
public class Solution968 {

    int count = 0;

    public int minCameraCover(TreeNode root) {
        int ans = dfs(root);
        return ans == 0 ? count + 1 : count;
    }

    public int dfs(TreeNode root) {
        if (root == null) {
            return 2;
        }

        int left = dfs(root.left);
        int right = dfs(root.right);

        // 左右节点都有覆盖，则当前节点为无覆盖
        if (left == 2 && right == 2) {
            return 0;
        }

        // 左右节点有至少一个无覆盖，则当前需要放摄像机
        if (left == 0 || right == 0) {
            count++;
            return 1;
        }

        // 左右节点至少有一个摄像机，则当前节点为有覆盖
        if (left == 1 || right == 1) {
            return 2;
        }

        return 0;
    }
}
