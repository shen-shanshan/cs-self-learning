import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 84.柱状图中最大的矩形
 */
public class Solution84 {
    public int largestRectangleArea(int[] heights) {
        int[] newHeights = new int[heights.length + 2];

        for (int i = 1; i < newHeights.length - 1; i++) {
            newHeights[i] = heights[i - 1];
        }

        int max = 0;

        // 存的是数组元素的下标
        Deque<Integer> stack = new LinkedList<>();

        stack.push(0);

        for (int i = 1; i < newHeights.length; i++) {
            while (newHeights[i] < newHeights[stack.peek()]) {
                int cur = stack.pop();
                int hig = newHeights[cur];
                int len = i - stack.peek() - 1;
                max = Math.max(max, hig * len);
            }
            stack.push(i);
        }

        return max;
    }
}

