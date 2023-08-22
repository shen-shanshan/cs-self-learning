/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    int res = 0;

    public int translateNum(int num) {
        String str = String.valueOf(num);
        backtrack(str, 0);
        return res;
    }

    public void backtrack(String str, int index) {
        // base case
        if (index == str.length()) {
            res++;
            return;
        }
        // 当前数字单独算一个字母
        backtrack(str, index + 1);
        // 判断当前数字及其下一个数字组成的数字是否在 25 以内
        if (index + 1 < str.length()) {
            String cur = str.substring(index, index + 2);
            int num = Integer.parseInt(cur);
            if (num > 9 && num < 26) {
                backtrack(str, index + 2);
            }
        }
    }

}
