import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution_217 {

    /* 暴力解法（超时）：
    public boolean containsDuplicate(int[] nums) {
        if (nums.length == 0) return false;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        return false;
    }*/

    /* 使用 HashSet<>()：
    public boolean containsDuplicate(int[] nums) {
        if (nums.length == 0) return false;
     遍历数组，数字放到 set 中。
     如果数字已经存在于 set 中，直接返回 true。
     如果成功遍历完数组，则表示没有重复元素，返回 false。
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }*/

    // 先排序，再遍历数组，判断相邻的数字是否相等：（时间效率，空间效率最高）
    public boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 4};
        int[] nums2 = {1, 3, 4, 3, 2, 4, 2};

        Solution_217 s = new Solution_217();
        boolean res = s.containsDuplicate(nums2);
        System.out.println(res);
    }
}
