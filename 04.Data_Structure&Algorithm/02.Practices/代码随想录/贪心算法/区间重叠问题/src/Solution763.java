import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 763.划分字母区间
 */
public class Solution763 {
    public List<Integer> partitionLabels(String s) {
        // 记录每个字母最后一次出现的位置
        int[] lastIndex = new int[26];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            lastIndex[c - 'a'] = i;
        }

        int right = 0;
        int count = 0;

        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            right = Math.max(right, lastIndex[c - 'a']);
            count++;
            if (i == right) {
                ans.add(count);
                count = 0;
            }
        }

        return ans;
    }
}
