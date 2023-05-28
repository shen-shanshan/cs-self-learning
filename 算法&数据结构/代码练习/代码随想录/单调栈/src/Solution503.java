import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 503.下一个更大元素 II
 */
public class Solution503 {
    public int[] nextGreaterElements(int[] nums) {
        int len = nums.length;

        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = -1;
        }

        // 存的是下标
        Deque<Integer> stack = new LinkedList<>();

        // 记录已经确定了结果的下标
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < len * 2; i++) {
            int index = i % len;
            // 弹栈
            while (!stack.isEmpty() && nums[stack.peek()] < nums[index]) {
                int cur = stack.pop();
                ans[cur] = nums[index];
                set.add(cur);
            }
            // 入栈
            if (!set.contains(index)) {
                stack.push(index);
            }
        }

        return ans;
    }
}
