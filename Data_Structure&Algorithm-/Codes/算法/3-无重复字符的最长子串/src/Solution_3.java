import java.util.HashMap;
import java.util.Map;

public class Solution_3 {
    /*public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        if (len == 0) return 0;
        if (len == 1) return 1;

        char[] c = s.toCharArray();
        int maxSubLength = 1;
        Map<Integer, Character> map = new HashMap<>();
        map.put(0, c[0]);
        int i = 0;
        int j = 1;

        while (j < len) {
            if (!map.containsValue(c[j])) {
                map.put(j, c[j]);
                j++;
            } else {
                map.remove(i);
                i++;
            }
            maxSubLength = Math.max(maxSubLength, j - i);
        }
        return maxSubLength;
    }*/

    // 改进：左指针 i 跳跃移动，直接跳到重复字符的下一个位置，时间效率得到了极大的提高
    public int lengthOfLongestSubstring(String s) {
        // 记录字符上一次出现的位置
        int[] last = new int[128];
        for(int i = 0; i < 128; i++) {
            last[i] = -1;
        }
        int n = s.length();
        // 记录最长子串的长度
        int res = 0;
        // 窗口开始位置
        int start = 0;
        for(int i = 0; i < n; i++) {
            int index = s.charAt(i);
            start = Math.max(start, last[index] + 1);
            res   = Math.max(res, i - start + 1);
            last[index] = i;
        }
        return res;
    }

    public static void main(String[] args) {
        String s1 = "abcabcbb";
        String s2 = "bbbbb";
        String s3 = "pwwkep";
        String s4 = "";

        Solution_3 s = new Solution_3();
        int res1 = s.lengthOfLongestSubstring(s1);
        int res2 = s.lengthOfLongestSubstring(s2);
        int res3 = s.lengthOfLongestSubstring(s3);
        int res4 = s.lengthOfLongestSubstring(s4);

        System.out.println(res1);// 3
        System.out.println(res2);// 1
        System.out.println(res3);// 3
        System.out.println(res4);// 0
    }
}
