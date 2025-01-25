import com.sun.org.apache.bcel.internal.generic.SWAP;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 31.下一个排列
 * 题解：
 * 例如：2, 6, 3, 5, 4, 1 这个排列，我们想要找到下一个刚好比他大的排列，于是可以从后往前看。
 * 我们先看后两位 4, 1 能否组成更大的排列，答案是不可以，同理 5, 4, 1 也不可以。
 * 直到 3, 5, 4, 1 这个排列，因为 3 < 5，我们可以通过重新排列这一段数字，来得到下一个排列。
 * 因为我们需要使得新的排列尽量小，所以我们从后往前找第一个比 3 更大的数字，发现是 4。
 * 然后，我们调换 3 和 4 的位置，得到 4, 5, 3, 1 这个数列。
 * 因为我们需要使得新生成的数列尽量小，于是我们可以对 5, 3, 1 进行排序。
 * 可以发现在这个算法中，我们得到的末尾数字一定是倒序排列的，于是我们只需要把它反转即可。
 * 最终，我们得到了 4, 1, 3, 5 这个数列，完整的数列则是 2, 6, 4, 1, 3, 5 。
 */
public class Solution31 {
    public void nextPermutation(int[] nums) {
        int len = nums.length;

        if (len == 1) {
            return;
        }

        // 从后往前遍历
        // 找到第一个不满足从后往前单调递增的数字
        int left = len - 2;
        while (left >= 0) {
            if (nums[left] < nums[left + 1]) {
                break;
            }
            left--;
        }

        // nums 单调递减
        if (left == -1) {
            myReverse(nums, 0, len - 1);
            return;
        }

        int target = nums[left];
        for (int i = len - 1; i > left; i--) {
            // 从后往前找到第一个比 target 大的数
            if (nums[i] > target) {
                nums[left] = nums[i];
                nums[i] = target;
                break;
            }
        }

        myReverse(nums, left + 1, len - 1);
    }

    public void myReverse(int[] arr, int start, int end) {
        int left = start;
        int right = end;

        while (left < right) {
            int tmp = arr[left];
            arr[left] = arr[right];
            arr[right] = tmp;
            left++;
            right--;
        }
    }
}
