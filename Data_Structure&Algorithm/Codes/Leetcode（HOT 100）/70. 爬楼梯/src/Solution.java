public class Solution {
    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        } else if (n == 2) {
            return 2;
        }
        int before = 1;
        int after = 2;
        int tmp = 0;
        for (int i = 3; i <= n; i++) {
            tmp = before + after;
            before = after;
            after = tmp;
        }
        return tmp;
    }
}
