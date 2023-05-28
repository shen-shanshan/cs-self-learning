import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 139.单词拆分
 */
public class Solution139 {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;

        // 应该先遍历物品，再遍历背包
        // 保证对每个背包大小，与所有物品都进行一次匹配
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
