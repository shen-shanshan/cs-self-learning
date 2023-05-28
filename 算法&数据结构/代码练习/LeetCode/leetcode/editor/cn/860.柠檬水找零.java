package editor.cn;

// 860.柠檬水找零
class Solution860 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean lemonadeChange(int[] bills) {
            // 动态记录三种钞票的数量
            int five = 0;
            int ten = 0;
            // int twenty = 0;

            for (int i = 0; i < bills.length; i++) {
                if (bills[i] == 5) {
                    // 直接收下
                    five++;
                } else if (bills[i] == 10) {
                    // 收下一张 10，支出一张 5
                    ten++;
                    if (five == 0) {
                        return false;
                    }
                    five--;
                } else {
                    // bills[i] == 20
                    // 收下一张 20
                    // twenty++;
                    if (ten > 0 && five > 0) {
                        // 优先找一张 10 和一张 5
                        ten--;
                        five--;
                    } else if (five >= 3) {
                        // 或者找 3 张 5
                        five -= 3;
                    } else {
                        return false;
                    }
                }
            }

            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}