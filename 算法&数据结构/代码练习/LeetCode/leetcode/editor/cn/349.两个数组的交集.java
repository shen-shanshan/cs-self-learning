package editor.cn;

import java.util.HashSet;
import java.util.Set;

// 349.两个数组的交集
class Solution349 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] intersection(int[] nums1, int[] nums2) {

            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < nums1.length; i++) {
                set.add(nums1[i]);
            }

            Set<Integer> ans = new HashSet<>();
            for (int i = 0; i < nums2.length; i++) {
                if (set.contains(nums2[i])) {
                    ans.add(nums2[i]);
                }
            }

            int[] res = new int[ans.size()];
            int i = 0;
            for (Integer x : ans) {
                res[i++] = x;
            }

            return res;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}