import java.util.Deque;
import java.util.LinkedList;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 84.柱状图中最大的矩形
 */
public class Solution84 {
    public int largestRectangleArea(int[] heights) {
        int[] arr = new int[heights.length + 2];
        int len = arr.length;

        for (int i = 1; i < len - 1; i++) {
            arr[i] = heights[i - 1];
        }

        // 存的是下标
        Deque<Integer> stack = new LinkedList<>();

        int max = 0;

        for (int i = 0; i < len; i++) {
            // 比栈顶元素大的才入栈
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int cur = stack.pop();
                int h = arr[cur];
                int w = i - stack.peek() - 1;
                max = Math.max(max, h * w);
            }
            stack.push(i);
        }

        return max;
    }
}
