import java.util.Arrays;
import java.util.Comparator;

/*
每个人在数组中的位置都是确定的,虽然此时我们还不知道,但位置都是固定好的。
我们先将矮的人排序,为什么这么做呢?
因为最矮的人对应的 ki 即所谓大于等于自己高度的人数是数组空位中的绝对下标（即对应于结果数组的下标）。
当最矮的人排完序后,第二矮的人就成了最矮的人。
同理将第二矮的人排在空位中的 ki 个位置,如此直至排完所有人。
*/
public class Solution {
    public int[][] reconstructQueue(int[][] people) {
        // 以身高为第一优先级（升序），ki 为第二优先级（降序）进行排序
        Arrays.sort(people, new Comparator<int[]>() {
            public int compare(int[] person1, int[] person2) {
                if (person1[0] != person2[0]) {
                    return person1[0] - person2[0];
                } else {
                    return person2[1] - person1[1];
                }
            }
        });
        int n = people.length;
        int[][] ans = new int[n][];
        for (int[] person : people) {
            int spaces = person[1] + 1;
            for (int i = 0; i < n; ++i) {
                if (ans[i] == null) {
                    --spaces;
                    if (spaces == 0) {
                        ans[i] = person;
                        break;
                    }
                }
            }
        }
        return ans;
    }
}