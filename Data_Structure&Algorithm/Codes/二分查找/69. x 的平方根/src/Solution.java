public class Solution {
    public int mySqrt(int x) {
        int left = 0;
        int right = x;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 需要考虑 int 类型溢出的问题
            long cur = (long) mid * mid;
            if (cur < x) {
                left = mid + 1;
            } else if (cur > x) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return right;
    }
}
