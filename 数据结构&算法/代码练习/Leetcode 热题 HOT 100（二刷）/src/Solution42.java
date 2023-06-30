import java.util.Deque;
import java.util.LinkedList;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 42.接雨水
 */
public class Solution42 {
    public int trap(int[] height) {
        int len = height.length;

        Deque<Integer> stack = new LinkedList<>();

        int ans = 0;

        for (int i = 0; i < len; i++) {
            if (!stack.isEmpty() && height[stack.peek()] == height[i]) {
                stack.pop();
            }
            while (!stack.isEmpty() && height[stack.peek()] <= height[i]) {
                int midIndex = stack.pop();
                if (!stack.isEmpty()) {
                    int right = height[i];
                    int left = height[stack.peek()];
                    int mid = height[midIndex];
                    ans += (Math.min(left, right) - mid) * (i - stack.peek() - 1);
                }
            }
            stack.push(i);
        }

        return ans;
    }
}
