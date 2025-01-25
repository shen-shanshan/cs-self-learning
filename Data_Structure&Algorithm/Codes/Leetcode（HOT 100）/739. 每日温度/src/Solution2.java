import java.util.Deque;
import java.util.LinkedList;

/*单调栈：
* 维护一个存储下标的单调栈，从栈底到栈顶的下标对应的温度列表中的温度依次递减。
* 如果一个下标在单调栈里，则表示尚未找到下一次温度更高的下标。*/
public class Solution2 {
    public int[] dailyTemperatures(int[] temperatures) {
        int length = temperatures.length;
        int[] ans = new int[length];
        Deque<Integer> stack = new LinkedList<Integer>();
        // 正向遍历
        for (int i = 0; i < length; i++) {
            int temperature = temperatures[i];
            // 重复下面的操作直到栈为空或者栈顶元素对应的温度大于当前温度，然后将 i 进栈
            while (!stack.isEmpty() && temperature > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                ans[prevIndex] = i - prevIndex;
            }
            stack.push(i);
        }
        return ans;
    }
}