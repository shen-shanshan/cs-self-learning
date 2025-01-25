public class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        int len = letters.length;
        int left = 0;
        int right = len - 1;
        // 特殊情况：letters 中最大的元素小于等于 target
        if (letters[len - 1] - target <= 0) {
            return letters[0];
        }
        // 一般情况，二分查找
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            int cur = letters[mid] - 'a';
            if (cur <= target - 'a') {
                left = mid + 1;
            } else if (cur > target - 'a') {
                right = mid;
            }
        }
        return letters[left];
    }
}
