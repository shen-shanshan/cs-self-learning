public class Solution {
    public int guessNumber(int n) {
        int left = 1;
        int right = n;
        int ret = -1;
        int mid = 0;
        while (ret != 0) {
            mid = left + ((right - left) >> 1);
            // ret = guess(mid);
            if (ret < 0) {
                right = mid - 1;
            } else if (ret > 0) {
                left = mid + 1;
            }
        }
        return mid;
    }
}
