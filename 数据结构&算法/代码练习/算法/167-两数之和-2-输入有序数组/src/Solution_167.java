import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution_167 {
    // 方法一：使用 HashMap 搜索
    // 使用哈希表需要 O(n) 的时间复杂度和 O(n) 的空间复杂度。
    // 但是这种解法是针对无序数组的，没有利用到输入数组有序的性质。
    // 利用输入数组有序的性质，可以得到时间复杂度和空间复杂度更优的解法。
    /*public int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        int n = numbers.length;
        int[] res = new int[2];
        for (int i = 0; i < n; i++) {
            int k = target - numbers[i];
            if (map.containsKey(k)) {
                res[0] = map.get(k) + 1;
                res[1] = i + 1;
                break;
            }
            map.put(numbers[i], i);
        }
        return res;
    }*/

    // 方法二：二分查找
    // 时间复杂度：O(nlogn)，其中 n 是数组的长度。需要遍历数组一次确定第一个数，时间复杂度是 O(n)。
    //           寻找第二个数使用二分查找，时间复杂度是 O(logn)，因此总时间复杂度是 O(nlogn)。
    // 空间复杂度：O(1)。
    /*public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length; ++i) {
            int low = i + 1, high = numbers.length - 1;
            while (low <= high) {
                int mid = (high - low) / 2 + low;
                if (numbers[mid] == target - numbers[i]) {
                    return new int[]{i + 1, mid + 1};
                } else if (numbers[mid] > target - numbers[i]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return new int[]{-1, -1};
    }*/

    // 方法三：双指针（效率最优）
    // 时间复杂度：O(n)，其中 n 是数组的长度。两个指针移动的总次数最多为 n 次。
    // 空间复杂度：O(1)。
    public int[] twoSum(int[] numbers, int target) {
        int low = 0, high = numbers.length - 1;
        while (low < high) {
            int sum = numbers[low] + numbers[high];
            if (sum == target) {
                return new int[]{low + 1, high + 1};
            } else if (sum < target) {
                ++low;
            } else {
                --high;
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] nums2 = {2, 3, 4};
        int target2 = 6;
        int[] nums3 = {-1, 0};
        int target3 = -1;

        Solution_167 s = new Solution_167();
        int[] res1 = s.twoSum(nums1, target1);
        int[] res2 = s.twoSum(nums2, target2);
        int[] res3 = s.twoSum(nums3, target3);

        for (int x : res1) {
            System.out.print(x + " ");
        }
        System.out.println();
        for (int x : res2) {
            System.out.print(x + " ");
        }
        System.out.println();
        for (int x : res3) {
            System.out.print(x + " ");
        }
    }
}
