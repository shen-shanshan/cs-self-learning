import java.util.ArrayList;
import java.util.List;

/*
* 滑动窗口优化：
* 引入变量 differ 来记录当前窗口与字符串 p 中数量不同的字母的个数，并在滑动窗口的过程中维护它。
* */
public class Solution3 {
    public List<Integer> findAnagrams(String s, String p) {
        int sLen = s.length(), pLen = p.length();

        if (sLen < pLen) {
            return new ArrayList<Integer>();
        }

        List<Integer> ans = new ArrayList<Integer>();
        int[] count = new int[26];
        for (int i = 0; i < pLen; ++i) {
            ++count[s.charAt(i) - 'a'];
            --count[p.charAt(i) - 'a'];
        }

        int differ = 0;
        for (int j = 0; j < 26; ++j) {
            if (count[j] != 0) {
                ++differ;
            }
        }

        if (differ == 0) {
            ans.add(0);
        }

        for (int i = 0; i < sLen - pLen; ++i) {
            if (count[s.charAt(i) - 'a'] == 1) {
                // 当前字符之前 s 有，p 没有
                --differ;
            } else if (count[s.charAt(i) - 'a'] == 0) {
                // 当前字符之前 s 和 p 都有
                ++differ;
            }
            --count[s.charAt(i) - 'a'];

            if (count[s.charAt(i + pLen) - 'a'] == -1) {
                // 当前字符之前 s 没有，p 有，现在 s 也有了，差别变小
                --differ;
            } else if (count[s.charAt(i + pLen) - 'a'] == 0) {
                // 当前字符之前 s 和 p 都有或者都没有，现在 s 有了，差别变大
                ++differ;
            }
            ++count[s.charAt(i + pLen) - 'a'];

            if (differ == 0) {
                ans.add(i + 1);
            }
        }

        return ans;
    }
}