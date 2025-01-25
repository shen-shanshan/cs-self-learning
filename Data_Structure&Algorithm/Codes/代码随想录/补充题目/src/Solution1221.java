/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 1221.分割平衡字符串
 */
public class Solution1221 {
    public int balancedStringSplit(String s) {
        int l = 0;
        int r = 0;

        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == 'L') {
                l++;
            } else {
                r++;
            }

            if (l == r) {
                count++;
                l = 0;
                r = 0;
            }
        }

        return count;
    }
}
