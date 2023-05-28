public class Solution {
    public int maxArea(int[] height) {
        if (height.length < 2) return 0;
        int max = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int s = Math.min(height[left], height[right]) * (right - left);
            max = Math.max(max, s);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] a1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int[] a2 = {1, 1};

        Solution s = new Solution();
        int ans1 = s.maxArea(a1);
        int ans2 = s.maxArea(a2);
        System.out.println(ans1);
        System.out.println(ans2);
    }
}
