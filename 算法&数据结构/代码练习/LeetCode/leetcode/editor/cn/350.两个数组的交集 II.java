package editor.cn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 350.两个数组的交集 II
class Solution350 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] intersect(int[] nums1, int[] nums2) {

            List<Integer> list = new LinkedList<>();

            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < nums1.length; i++) {
                map.put(nums1[i], map.getOrDefault(nums1[i], 0) + 1);
            }

            for (int i = 0; i < nums2.length; i++) {
                int count = map.getOrDefault(nums2[i], 0);
                if (count == 1) {
                    list.add(nums2[i]);
                    map.remove(nums2[i]);
                } else if (count > 1) {
                    list.add(nums2[i]);
                    map.put(nums2[i], count - 1);
                }
            }

            int[] ans = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ans[i] = list.get(i);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}