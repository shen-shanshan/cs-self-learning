import java.util.Random;

/* 官方答案：快速选择算法
 * 我们一定可以确定一个元素的最终位置，即 x 的最终位置为 q，并且保证：
 * a[left⋯q−1] 中的每个元素小于等于 a[q]，且 a[q] 小于等于 a[q+1⋯right] 中的每个元素。
 * 所以只要某次划分的 q 为倒数第 k 个下标的时候，我们就已经找到了答案。
 * 至于 a[left⋯q−1] 和 a[q+1⋯right] 是否是有序的，我们不关心。
 * 如果划分得到的 q 正好就是我们需要的下标，就直接返回 a[q];
 * 如果 q 比目标下标小，就递归右子区间，否则递归左子区间。
 * 这样就可以把原来递归两个区间变成只递归一个区间，提高了时间效率。
 * */
public class Solution2 {
    Random random = new Random();

    public int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    public int quickSelect(int[] a, int l, int r, int index) {
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
        int x = a[r], i = l - 1;
        for (int j = l; j < r; ++j) {
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