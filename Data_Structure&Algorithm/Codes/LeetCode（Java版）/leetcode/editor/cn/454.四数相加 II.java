package editor.cn;

import java.util.HashMap;
import java.util.Map;

// 454.四数相加 II
class Solution454 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {

            // 统计前两个数组的两数之和
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < nums1.length; i++) {
                for (int j = 0; j < nums2.length; j++) {
                    int cur = nums1[i] + nums2[j];
                    map.put(cur, map.getOrDefault(cur, 0) + 1);
                }
            }

            int ans = 0;
            // 统计后两个数组中能与前面的结果凑成 0 的次数
            for (int i = 0; i < nums3.length; i++) {
                for (int j = 0; j < nums4.length; j++) {
                    int cur = nums3[i] + nums4[j];
                    int count = map.getOrDefault(-cur, 0);
                    ans += count;
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}