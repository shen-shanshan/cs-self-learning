import java.util.HashMap;

public class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0 || s.length() == 1) return s.length();
        // 存每一个字符上一次出现的位置
        HashMap<Character, Integer> map = new HashMap<>();
        int start = 0;
        map.put(s.charAt(0), 0);
        int max = 1;
        for (int i = 1; i < s.length(); i++) {
            start = Math.max(start, map.getOrDefault(s.charAt(i), -1) + 1);
            max = Math.max(max, i - start + 1);
            map.put(s.charAt(i), i);
        }
        return max;
    }

    public static void main(String[] args) {
        String str1 = "abcabcbb";
        String str2 = "bbbbb";
        String str3 = "pwwkew";
        String str4 = "tmmzuxt";

        Solution s = new Solution();
        int res = s.lengthOfLongestSubstring(str4);
        System.out.println(res);
    }

    // 下面这个解法更好
    /*public int lengthOfLongestSubstring(String s) {
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
    }*/
}


