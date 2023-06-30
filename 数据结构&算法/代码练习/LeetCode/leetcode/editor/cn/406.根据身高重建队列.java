package editor.cn;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// 406.根据身高重建队列
class Solution406 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[][] reconstructQueue(int[][] people) {
            Arrays.sort(people, new myComparator());

            int len = people.length;
            List<int[]> list = new LinkedList<>();

            for (int i = 0; i < len; i++) {
                list.add(people[i][1], people[i]);
            }

            return list.toArray(new int[len][]);
        }
    }

    class myComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            // 优先按身高从高到低
            if (o1[0] != o2[0]) {
                return o2[0] - o1[0];
            }
            // 身高相等时，按 ki 从低到高
            return o1[1] - o2[1];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}