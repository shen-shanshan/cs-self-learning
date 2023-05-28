public class Solution2 {
    // 设第 i 个元素 ai 的缺失元素个数为 pi
    // 要找 pi-1 < k <= pi
    // 答案为 ai-1 + k - pi-1
    public int findKthPositive(int[] arr, int k) {
        // 最终 i = 0，找不到 i - 1，所以需要单独判断
        if (arr[0] > k) {
            return k;
        }

        int l = 0, r = arr.length;
        while (l < r) {
            int mid = (l + r) >> 1;
            // 当最后一个元素 pn-1 < k 时，找不到一个 >= k 的 pi
            // 可以在 arr 的最后加一个特别大的虚拟值，保证一定能找到一个 >= k 的 pi
            int x = mid < arr.length ? arr[mid] : Integer.MAX_VALUE;
            if (x - mid - 1 >= k) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }

        return k - (arr[l - 1] - (l - 1) - 1) + arr[l - 1];
    }
}
