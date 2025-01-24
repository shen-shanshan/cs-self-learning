import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solution {
    public int longestConsecutive(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        // 已出现的数字 <—> 当前数可以组成的最大序列长度
        HashMap<Integer, Integer> map = new HashMap<>();
        // 动态规划
        for (int i = 0; i < len; i++) {
            if (!map.containsKey(nums[i])) {
                int left = map.getOrDefault(nums[i] - 1, 0);
                int right = map.getOrDefault(nums[i] + 1, 0);
                int curLen = left + 1 + right;
                map.put(nums[i], curLen);
                // 更新序列端点的数据
                map.put(nums[i] - left, curLen);
                map.put(nums[i] + right, curLen);
            }
            // 若当前数已经存在于表中，则直接跳过
        }
        Set<Map.Entry<Integer, Integer>> m = map.entrySet();
        int max = 0;
        for (Map.Entry<Integer, Integer> i : m) {
            max = Math.max(max, i.getValue());
        }
        return max;
    }
}
