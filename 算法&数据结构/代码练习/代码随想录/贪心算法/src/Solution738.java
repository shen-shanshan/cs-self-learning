/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 738.单调递增的数字
 */
public class Solution738 {
    public int monotoneIncreasingDigits(int n) {
        String num = String.valueOf(n);
        char[] chars = num.toCharArray();

        // 记录已经变为 9 的左边界
        int nine = num.length();

        // 从右到左遍历
        for (int i = num.length() - 1; i > 0; i--) {
            if (chars[i - 1] > chars[i]) {
                chars[i - 1]--;
                for (int j = i; j < nine; j++) {
                    chars[j] = '9';
                }
                nine = i;
            }
        }

        return Integer.parseInt(String.valueOf(chars));
    }
}
