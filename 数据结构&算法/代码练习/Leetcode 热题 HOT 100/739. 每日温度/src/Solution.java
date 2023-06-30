import java.util.Arrays;

// 暴力：从后向前遍历
public class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int length = temperatures.length;
        int[] ans = new int[length];
        // next 从右向左记录每个温度第一次出现的下标，其中的元素初始化为无穷大
        int[] next = new int[101];
        Arrays.fill(next, Integer.MAX_VALUE);
        // 反向遍历温度列表
        for (int i = length - 1; i >= 0; --i) {
            int warmerIndex = Integer.MAX_VALUE;
            for (int t = temperatures[i] + 1; t <= 100; ++t) {
                // 对于 temperatures 中的每一个元素，找到比当前高的温度中的最小下标
                if (next[t] < warmerIndex) {
                    warmerIndex = next[t];
                }
            }
            // 如果后面存在比当前还高的温度，则更新 ans
            if (warmerIndex < Integer.MAX_VALUE) {
                ans[i] = warmerIndex - i;
            }
            // 否则默认为 ans[i] = 0
            // 更新 next 数组，加入当前温度
            next[temperatures[i]] = i;
        }
        return ans;
    }
}