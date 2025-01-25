import java.util.Random;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 215.数组中的第K个最大元素
 */
public class Solution215 {

    Random random = new Random();

    public int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    public int quickSelect(int[] a, int l, int r, int index) {
        // 随机化 + 分层
        // q: 分层结果
        int q = randomPartition(a, l, r);

        if (q == index) {
            return a[q];
        } else {
            return q < index ? quickSelect(a, q + 1, r, index) : quickSelect(a, l, q - 1, index);
        }
    }

    public int randomPartition(int[] a, int l, int r) {
        int i = random.nextInt(r - l + 1) + l;
        swap(a, i, r);
        return partition(a, l, r);
    }

    // 只分为左右两部分，没有等于区
    // 完成分层后，左边的数都小于等于 a[i+1]，右边的数都大于等于 a[i+1]
    public int partition(int[] a, int l, int r) {
        int x = a[r];

        // 小于等于区的边界
        int i = l - 1;

        for (int j = l; j < r; j++) {
            if (a[j] <= x) {
                swap(a, ++i, j);
            }
        }

        swap(a, i + 1, r);

        return i + 1;
    }

    public void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
