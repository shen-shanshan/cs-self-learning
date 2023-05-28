import java.util.HashMap;

public class Solution {
    public int subarraySum(int[] nums, int k) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int count = 0;
        // HashMap: <前缀和，该总和出现的次数>
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        map.put(0, 1);
        for (int i = 0; i < len; i++) {
            sum += nums[i];
            // 这里需要注意下面两句的顺序，一定不能交换
            // 交换后，nums = {1}，k = 0 时结果不正确
            count += map.getOrDefault(sum - k, 0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}