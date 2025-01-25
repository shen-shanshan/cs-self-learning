public class Solution_977 {
    // 方法一：选择排序
    // 时间效率低，使用双重 for 循环，耗时太久。
    // 空间效率高，直接在原数组上进行操作。
    /*public int[] sortedSquares(int[] nums) {
        if (nums == null) return null;
        if (nums.length == 1) {
            nums[0] = nums[0] * nums[0];
            return nums;
        }
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] * nums[i] > nums[j] * nums[j]) {
                    int tmp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = tmp;
                }
            }
            nums[i] = nums[i] * nums[i];
        }
        nums[nums.length - 1] = nums[nums.length - 1] * nums[nums.length - 1];
        return nums;
    }*/

    // 方法二：归并排序
    // 时间复杂度：O(n)。其中 n 是数组 nums 的长度。
    // 空间复杂度：O(1)。除了存储答案的数组以外，我们只需要维护常量空间。
    /*public int[] sortedSquares(int[] nums) {
        if (nums == null) return null;
        int len = nums.length;
        if (len == 1) {
            nums[0] = nums[0] * nums[0];
            return nums;
        }
        // 1.数组元素全是非负数，将每个数平方后，直接返回。
        if (nums[0] >= 0) {
            // 这里只能使用 for 循环遍历并修改数组。
            // 若使用增强 for 则只能遍历，但并不能修改数组中的值。
            for (int i = 0; i < len; i++) {
                nums[i] *= nums[i];
            }
            return nums;
        }
        // 2.数组元素全是非正数，将每个数平方后，逆序返回。
        if (nums[len - 1] <= 0) {
            int i = 0;
            int j = len - 1;
            for (int k = 0; k < len; k++) {
                nums[k] *= nums[k];
            }
            while (i < j) {
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
                i++;
                j--;
            }
            return nums;
        }
        // 3.数组元素有正有负
        // 找到正负数的分界线，将原数组拆分为两个有序的子数组。0 ~ div 为负数。
        int div = 0;
        for (int i = 0; i < len - 1; i++) {
            if (nums[i] < 0 && nums[i + 1] >= 0) {
                div = i;
                break;
            }
        }
        // 将每个数平方
        for (int k = 0; k < len; k++) {
            nums[k] *= nums[k];
        }
        // 将原数组划分为 0 ~ div，以及 div+1 ~ len-1，使用双指针进行归并排序
        int i = div;
        int j = div + 1;
        int k = 0;
        int[] res = new int[len];
        while (i >= 0 && j < len) {
            if (nums[i] < nums[j]) {
                res[k++] = nums[i];
                i--;
            } else {
                res[k++] = nums[j];
                j++;
            }
        }
        if (i < 0) {
            while (j < len) {
                res[k++] = nums[j++];
            }
        } else if (j == len) {
            while (i >= 0) {
                res[k++] = nums[i--];
            }
        }
        return res;
    }*/

    // 改进：官方版本
    /*public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int negative = -1;
        for (int i = 0; i < n; ++i) {
            if (nums[i] < 0) {
                negative = i;
            } else {
                break;
            }
        }
        int[] ans = new int[n];
        int index = 0, i = negative, j = negative + 1;
        while (i >= 0 || j < n) {
            if (i < 0) {
                ans[index] = nums[j] * nums[j];
                ++j;
            } else if (j == n) {
                ans[index] = nums[i] * nums[i];
                --i;
            } else if (nums[i] * nums[i] < nums[j] * nums[j]) {
                ans[index] = nums[i] * nums[i];
                --i;
            } else {
                ans[index] = nums[j] * nums[j];
                ++j;
            }
            ++index;
        }
        return ans;
    }*/

    // 方法三：双指针
    // 时间复杂度：O(n)。其中 n 是数组 nums 的长度。
    // 空间复杂度：O(1)。除了存储答案的数组以外，我们只需要维护常量空间。
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        for (int i = 0, j = n - 1, pos = n - 1; i <= j; ) {
            if (nums[i] * nums[i] > nums[j] * nums[j]) {
                ans[pos] = nums[i] * nums[i];
                ++i;
            } else {
                ans[pos] = nums[j] * nums[j];
                --j;
            }
            --pos;
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums1 = {-4, -1, 0, 3, 10};
        int[] nums2 = {-7, -3, 2, 3, 11};
        // int[] nums3 = {1, 2, 4, 6, 8};
        int[] nums3 = {-8, -6, -4, -2, 0};

        Solution_977 s = new Solution_977();
        int[] res1 = s.sortedSquares(nums1);
        int[] res2 = s.sortedSquares(nums2);
        int[] res3 = s.sortedSquares(nums3);

        for (int x : res1) {
            System.out.print(x + " ");// 0, 1, 9, 16, 100
        }
        System.out.println();
        for (int x : res2) {
            System.out.print(x + " ");// 4, 9, 9, 49, 121
        }
        System.out.println();
        for (int x : res3) {
            System.out.print(x + " ");// 0, 4, 16, 36, 64
        }
    }
}
