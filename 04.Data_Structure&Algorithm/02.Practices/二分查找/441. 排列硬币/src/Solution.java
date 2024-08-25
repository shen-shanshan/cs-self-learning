public class Solution {
    public int arrangeCoins(int n) {
        if (n == 1) {
            return 1;
        }
        int left = 1;
        int right = n;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            // int 可能溢出
            long sum = (long) (1 + mid) * mid / 2;
            if (sum < n) {
                left = mid + 1;
            } else if (sum > n) {
                right = mid;
            } else {
                return mid;
            }
        }
        return left - 1;
    }
}
