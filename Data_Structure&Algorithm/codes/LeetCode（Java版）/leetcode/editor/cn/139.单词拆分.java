package editor.cn;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 139.单词拆分
class Solution139 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean wordBreak(String s, List<String> wordDict) {
            Set<String> set = new HashSet<>(wordDict);

            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;

            for (int i = 1; i <= s.length(); i++) {
                for (int j = 0; j < i; j++) {
                    String cur = s.substring(j, i);
                    if (set.contains(cur) && dp[j]) {
                        dp[i] = true;
                        break;
                    }
                }
            }

            return dp[s.length()];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}