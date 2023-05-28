import java.util.Arrays;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 455.分发饼干
 */
public class Solution455 {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);

        int ans = 0;

        int i = 0;
        int j = 0;

        while (i < g.length) {
            while (j < s.length && g[i] > s[j]) {
                j++;
            }
            if (j == s.length) {
                break;
            }
            i++;
            j++;
            ans++;
        }

        return ans;
    }
}
