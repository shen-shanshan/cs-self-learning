public class Solution {
    public boolean judgeSquareSum(int c) {
        // 双指针
        // 假设 left <= right，left 从 0 开始变大，right 从根号 c 开始变小
        long left = 0;
        long right = (long) Math.sqrt(c);
        while (left <= right) {
            long sum = left * left + right * right;
            if (sum == c) {
                return true;
            } else if (sum > c) {
                right--;
            } else {
                left++;
            }
        }
        return false;
    }
}
