package editor.cn;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 202.快乐数
class Solution202 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isHappy(int n) {

            // 记录出现过的结果，用于判断是否进入循环
            Set<Integer> count = new HashSet<>();

            while (n != 1 && !count.contains(n)) {
                count.add(n);
                n = getSum(n);
            }

            return n == 1;
        }

        public int getSum(int n) {
            int ans = 0;
            while (n > 0) {
                ans += (n % 10) * (n % 10);
                n /= 10;
            }
            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}