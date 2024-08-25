public class Solution_70 {
    public int climbStairs(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        // int[] dp = new int[n];
        // dp[0] = 1;
        // dp[1] = 2;
        int a = 1;
        int b = 2;
        int tmp = 0;
        for (int i = 2; i < n; i++) {
            // dp[i] = dp[i - 1] + dp[i - 2];
            tmp = a + b;
            a = b;
            b = tmp;
        }
        // return dp[n - 1];
        return tmp;
    }

    public static void main(String[] args) {
        int n = 5;
        Solution_70 s = new Solution_70();
        int res = s.climbStairs(n);
        System.out.println(res);
    }
}
