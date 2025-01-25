import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*动态规划
* dp[i] 表示字符串 s 前 i 个字符组成的字符串 s[0..i−1] 是否能被空格拆分成若干个字典中出现的单词。
* 我们需要枚举 s[0..i-1] 中的分割点 j ，看 s[0..j-1] 组成的字符串 s1（默认 j = 0 时 s1为空串）
* 和 s[j..i-1] 组成的字符串 s2是否都合法。
* dp[i] = dp[j] && check(s[j..i−1])
* */
public class Solution2 {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordDictSet = new HashSet(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDictSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }
}