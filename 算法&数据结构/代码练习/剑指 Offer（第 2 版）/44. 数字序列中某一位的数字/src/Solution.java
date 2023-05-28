/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    /* 数字范围    数量  位数    占多少位
    1-9        9      1       9
    10-99      90     2       180
    100-999    900    3       2700
    1000-9999  9000   4       36000  ...

    例如 2901 = 9 + 180 + 2700 + 12 即一定是4位数,第12位   n = 12;
    数据为 = 1000 + (12 - 1)/ 4  = 1000 + 2 = 1002
    定位1002中的位置 = (n - 1) %  4 = 3    s.charAt(3) = 2;
    */

    public int findNthDigit(int n) {
        // 10 + 90 * 2 + 900 * 3 + ...
        if (n < 10) {
            // n = 0 ~ 9
            return n;
        }
        // 字符序列的长度
        long count = 10;
        long last = 0;
        // 当前数字范围的数字位数
        int i = 1;
        // n >= 10
        while (count <= n) {
            i++;
            long cur = 9;
            for (int j = 1; j < i; j++) {
                cur *= 10;
            }
            last = count;
            count += cur * i;
        }
        // num > n 时跳出循环
        System.out.println(i);
        long num = 1;
        int k = i;
        while (k > 1) {
            num *= 10;
            k--;
        }
        num += (n - last) / i;
        int position = (int) (n - last) % i;
        return String.valueOf(num).charAt(position) - '0';
    }

}
