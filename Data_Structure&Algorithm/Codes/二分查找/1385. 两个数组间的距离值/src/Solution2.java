import java.util.Arrays;

public class Solution2 {
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        Arrays.sort(arr2);
        int cnt = 0;
        for (int x : arr1) {
            // 寻找最接近 x 的 p 且满足：arr[p-1] < x < arr[p]
            int p = binarySearch(arr2, x);
            boolean ok = true;
            if (p < arr2.length) {
                ok &= arr2[p] - x > d;
            }
            if (p - 1 >= 0 && p - 1 <= arr2.length) {
                ok &= x - arr2[p - 1] > d;
            }
            cnt += ok ? 1 : 0;
        }
        return cnt;
    }

    public int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        // 若 arr 的最大值都比 target 小，则只存在 arr[p-1] < x，返回 p
        if (arr[high] < target) {
            return high + 1;
        }
        // 二分查找
        while (low < high) {
            // 当 [...,left(mid),right,...]
            // 且 arr[mid] < target，target < arr[right]
            // 然后 left + 1，left = right，跳出循环
            int mid = (high - low) / 2 + low;
            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        // 像下面这样进行二分也可以：
        /*while (low <= high) {
            int mid = (high - low) / 2 + low;
            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }*/
        // 返回的是比 target 大的数的下标
        return low;
    }
}