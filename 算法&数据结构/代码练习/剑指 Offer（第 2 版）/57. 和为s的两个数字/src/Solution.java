import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    // 哈希表，超时了
    public int[] twoSum(int[] nums, int target) {
        int len = nums.length;
        int[] ans = new int[]{};
        if (len < 2) {
            return ans;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            if (map.containsValue(target - nums[i])) {
                return new int[]{target - nums[i], nums[i]};
            }
            map.put(i, nums[i]);
        }
        return ans;
    }

}
