public class Solution {
    public boolean isPerfectSquare(int num) {
        if (num == 1) {
            return true;
        }
        int left = 1;
        int right = num;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 相乘以后可能会出现 int 溢出的情况，因此应用 long 来接收平方
            long tmp = (long) mid * mid;
            if (tmp < num) {
                left = mid + 1;
            } else if (tmp > num) {
                right = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }
}
