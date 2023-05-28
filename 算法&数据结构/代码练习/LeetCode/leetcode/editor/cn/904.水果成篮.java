package editor.cn;

import java.util.HashMap;
import java.util.Map;

// 904.水果成篮
class Solution904 {

    public static void main(String[] args) {
        Solution solution = new Solution904().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int totalFruit(int[] fruits) {
            // 记录窗口的最大长度（最多可以拿到的水果数）
            int len = 0;
            // 记录已经拿到的水果，key 为水果种类，value 为最后该种类一次出现的下标
            Map<Integer, Integer> map = new HashMap<>();
            // 记录当前窗口中拿的水果种类（分先后顺序）
            int first = 0;
            int second = 0;
            // 滑动窗口遍历
            int left = 0;
            int right = 0;
            while (right < fruits.length) {
                if (map.size() == 0) {
                    first = fruits[right];
                } else if (map.size() == 1 && !map.containsKey(fruits[right])) {
                    second = fruits[right];
                } else if (map.size() == 2 && map.containsKey(fruits[right])) {
                    // 拿了与之前两种水果种类重复的水果
                    if (fruits[right] == first) {
                        int tmp = first;
                        first = second;
                        second = tmp;
                    }
                } else if (map.size() == 2 && !map.containsKey(fruits[right])) {
                    // 出现了第三种水果
                    // 移除第一个种类，并移动窗口
                    int index = map.get(first);
                    left = index + 1;
                    map.remove(first);
                    // 更新种类顺序
                    first = second;
                    second = fruits[right];
                }
                map.put(fruits[right], right);
                len = Math.max(right - left + 1, len);
                right++;
            }
            return len;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}