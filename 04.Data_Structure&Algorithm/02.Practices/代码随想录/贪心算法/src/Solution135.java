/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 135.分发糖果
 */
public class Solution135 {
    public int candy(int[] ratings) {
        int len = ratings.length;

        if (len == 1) {
            return 1;
        }

        int[] ans = new int[len];

        // 1.从左到右
        ans[0] = 1;
        for (int i = 1; i < len; i++) {
            if (ratings[i] > ratings[i - 1]) {
                ans[i] = ans[i - 1] + 1;
            } else {
                ans[i] = 1;
            }
        }

        // 2.从右到左
        for (int i = len - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                ans[i] = Math.max(ans[i], ans[i + 1] + 1);
            }
        }

        int count = 0;

        for (int i = 0; i < len; i++) {
            count += ans[i];
        }

        return count;
    }
}
