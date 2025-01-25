import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 207.课程表
 */
public class Solution207 {

    List<List<Integer>> edges;

    int[] visited;

    boolean valid = true;

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        edges = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
        }

        // 为所有先修课程添加其后续课程
        for (int[] info : prerequisites) {
            // 节点 info[1] 为先修课程
            edges.get(info[1]).add(info[0]);
        }

        visited = new int[numCourses];

        for (int i = 0; i < numCourses && valid; i++) {
            // 刚开始所有节点的状态都是未搜索
            // 未搜索：我们还没有搜索到这个节点
            if (visited[i] == 0) {
                dfs(i);
            }
        }

        return valid;
    }

    public void dfs(int u) {
        // 将当前节点设为搜索中
        // 搜索中：我们搜索过这个节点，但还没有回溯到该节点
        // 即该节点还没有入栈，还有相邻的节点没有搜索完成
        visited[u] = 1;

        // 搜索
        for (int v: edges.get(u)) {
            if (visited[v] == 0) {
                dfs(v);
            } else if (visited[v] == 1) {
                // 如果有相邻的节点也处于搜索中，则说明图中有环
                valid = false;
                return;
            }
            // 若遇到 visited[v] == 2，则直接跳过，该分支已经搜索完成且没有坏
        }

        // 当前节点及其相邻节点搜索完成
        // 已完成：我们搜索过并且回溯过这个节点
        // 即该节点已经入栈，并且所有该节点的相邻节点都出现在栈的更底部的位置，满足拓扑排序的要求
        visited[u] = 2;
    }
}
