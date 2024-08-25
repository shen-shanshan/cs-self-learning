/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 860.柠檬水找零
 */
public class Solution860 {
    public boolean lemonadeChange(int[] bills) {
        int len = bills.length;

        int five = 0;
        int ten = 0;
        // int twenty = 0;

        for (int i = 0; i < len; i++) {
            if (bills[i] == 5) {
                five++;
            } else if (bills[i] == 10) {
                if (five > 0) {
                    five--;
                    ten++;
                } else {
                    return false;
                }
            } else {
                if (ten > 0 && five > 0) {
                    ten--;
                    five--;
                    // twenty++;
                } else if (five >= 3) {
                    five -= 3;
                } else {
                    return false;
                }
            }
        }

        return true;
    }
}
