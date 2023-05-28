import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;

        // 长短数组重定向
        int[] s = len1 < len2 ? nums1 : nums2;
        int[] l = s == nums1 ? nums2 : nums1;

        HashMap<Integer, Integer> map = new HashMap<>();

        // 先遍历较短的数组，并将每个数字的统计结果存入哈希表
        for (int i = 0; i < s.length; i++) {
            map.put(s[i], map.getOrDefault(s[i], 0) + 1);
        }

        ArrayList<Integer> ret = new ArrayList<>();
        // 遍历第二个数组，并动态查找、修改哈希表
        for (int i = 0; i < l.length; i++) {
            int count = map.getOrDefault(l[i], 0);
            if (count > 0) {
                ret.add(l[i]);
                map.put(l[i], count - 1);
            }
        }

        int[] ans = new int[ret.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = ret.get(i);
        }

        return ans;
    }
}
