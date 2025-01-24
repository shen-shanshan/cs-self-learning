import java.util.Arrays;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution2 {

    public String reverseLeftWords(String s, int n) {
        int len = s.length();
        s = reverse(s, 0, len - 1);
        s = reverse(s, 0, len - n - 1);
        s = reverse(s, len - n, len - 1);
        return s;
    }

    public String reverse(String s, int start, int end) {
        char[] chars = s.toCharArray();
        int left = start;
        int right = end;
        while (left < right) {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
            left++;
            right--;
        }
        return String.valueOf(chars);
    }

}
