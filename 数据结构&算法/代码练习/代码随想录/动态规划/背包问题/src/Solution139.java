import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 139.单词拆分
 */
public class Solution139 {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                String cur = s.substring(j, i);
                if (dp[j] && wordDict.contains(cur)) {
                    dp[i] = true;
                }
            }
        }

        return dp[s.length()];
    }
}
