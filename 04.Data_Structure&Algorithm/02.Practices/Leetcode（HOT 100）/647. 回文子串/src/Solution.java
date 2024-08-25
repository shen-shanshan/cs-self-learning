public class Solution {
    public int countSubstrings(String s) {
        int num = 0;
        int n = s.length();
        // 遍历回文中心点
        for (int i = 0; i < n; i++) {
            // j=0，中心是一个点；j=1，中心是两个点
            for (int j = 0; j <= 1; j++) {
                int l = i;
                int r = i + j;
                while (l >= 0 && r < n && s.charAt(l--) == s.charAt(r++)) {
                    num++;
                }
            }
        }
        return num;
    }
}