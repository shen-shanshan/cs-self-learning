package editor.cn;

// 135.分发糖果
class Solution135 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int candy(int[] ratings) {
            int len = ratings.length;
            int[] candy = new int[len];

            // 从左向右，计算左孩子 < 右孩子的情况
            for (int i = 0; i < len; i++) {
                if (i >0 && ratings[i] > ratings[i - 1]) {
                    candy[i] = candy[i - 1] + 1;
                }else{
                    candy[i] = 1;
                }
            }

            // 从右向左，计算左孩子 > 右孩子的情况
            int sum = 0;
            for (int i = len - 1; i > 0; i--) {
                sum += candy[i];
                if (ratings[i - 1] > ratings[i]) {
                    candy[i - 1] = Math.max(candy[i - 1], candy[i] + 1);
                }
            }
            sum += candy[0];

            return sum;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}