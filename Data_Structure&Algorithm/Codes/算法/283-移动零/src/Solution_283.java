public class Solution_283 {
    public void moveZeroes(int[] nums) {
        int n = nums.length;
        if (n == 0 || n == 1) return;

        // 方法一：
        /*int i = 0;
        while (i < n) {
            if (nums[i] == 0) {
                int j = i + 1;
                while (j < n && nums[j] == 0) j++;
                if (j == n) {
                    break;
                } else {
                    int tmp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = tmp;
                }
            }
            i++;
        }*/

        // 方法二：改进版双指针
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] != 0) {
                int tmp = nums[j];
                nums[j++] = nums[i];
                nums[i] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {0, 1, 0, 3, 12};
        //int[] nums = {0, 0, 0, 0, 0};
        //int[] nums = {5, 8, 9, 1, 2};

        Solution_283 s = new Solution_283();
        s.moveZeroes(nums);
        for (int x : nums) {
            System.out.print(x + " ");
        }
    }
}
