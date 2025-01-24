package editor.cn;

// 134.加油站
class Solution134 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int canCompleteCircuit(int[] gas, int[] cost) {
            int len = gas.length;
            int gasSum = 0;
            int costSum = 0;
            int[] rest = new int[len];

            for (int i = 0; i < gas.length; i++) {
                gasSum += gas[i];
                costSum += cost[i];
                rest[i] = gas[i] - cost[i];
            }

            // 不可能走完一圈
            if (gasSum < costSum) {
                return -1;
            }

            // 肯定能走完一圈
            int start = 0;
            int i = 0;
            int curSum = 0;
            while (i < len) {
                curSum += rest[i];
                if (curSum < 0) {
                    curSum = 0;
                    start = i + 1;
                }
                i++;
            }

            return start;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}