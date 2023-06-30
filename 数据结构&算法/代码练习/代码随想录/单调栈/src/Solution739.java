import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 739.每日温度
 */
public class Solution739 {
    public int[] dailyTemperatures(int[] temperatures) {
        int len = temperatures.length;

        int[] ans = new int[len];

        // 存的是下标
        Deque<Integer> stack = new LinkedList<Integer>();

        for (int i = 0; i < len; i++) {
            // 弹栈：直到栈顶元素 <= 当前元素 或 栈为空
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int index = stack.pop();
                ans[index] = i - index;
            }
            // 当前元素入栈
            stack.push(i);
        }

        return ans;
    }
}
