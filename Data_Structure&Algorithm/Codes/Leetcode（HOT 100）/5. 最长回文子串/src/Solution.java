public class Solution {
    // 中心扩展
    // 枚举所有的「回文中心」并尝试「扩展」，直到无法扩展为止。
    // 此时的回文串长度即为此「回文中心」下的最长回文串长度。
    // 我们对所有的长度求出最大值，即可得到最终的答案。
    // 时间复杂度：O(n^2)，其中 n 是字符串的长度。
    //           长度为 1 和 2 的回文中心分别有 n 和 n-1 个，每个回文中心最多会向外扩展 O(n) 次。
    // 空间复杂度：O(1)。
    public String longestPalindrome(String s) {
        if (s.length() == 0 || s.length() == 1) {
            return s;
        }
        // 记录最后要返回的字符串的下标范围
        int start = 0;
        int end = 0;
        // 从每一个回文中心向外扩展
        for (int i = 0; i < s.length(); i++) {
            // 回文中心是一个数
            int len1 = extend(s, i, i);
            // 回文中心是两个数
            int len2 = extend(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len >= (end - start + 1)) {
                start = i - ((len - 1) >> 1);
                end = i + (len >> 1);
            }
        }
        return s.substring(start, end + 1);
    }

    public int extend(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    public static void main(String[] args) {
        String s1 = "babad";
        String s2 = "cbbd";

        Solution s = new Solution();
        String res = s.longestPalindrome(s1);
        System.out.println(res);
    }
}
