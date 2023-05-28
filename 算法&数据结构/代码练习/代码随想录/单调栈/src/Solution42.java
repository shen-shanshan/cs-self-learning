import java.util.Deque;
import java.util.LinkedList;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 42.接雨水
 */
public class Solution42 {
    public int trap(int[] height) {
        int len = height.length;

        int sum = 0;

        // 存的是下标
        Deque<Integer> stack = new LinkedList<>();

        for (int i = 0; i < len; i++) {
            // 出栈
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                // 当前凹槽
                int index = stack.pop();
                // 计算雨水
                if (!stack.isEmpty()) {
                    int left = height[stack.peek()];
                    int mid = height[index];
                    int right = height[i];

                    int h = Math.min(left, right) - mid;
                    int w = i - stack.peek() - 1;

                    sum += h * w;
                    // System.out.println("结束位置：" + i + "  面积： " + h * w);
                }
            }
            stack.push(i);
        }

        return sum;
    }
}
