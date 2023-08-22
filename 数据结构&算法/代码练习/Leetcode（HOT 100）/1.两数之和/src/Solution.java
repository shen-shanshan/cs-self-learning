import java.util.HashMap;

public class Solution {
    public int[] twoSum(int[] nums, int target) {
        if (nums.length < 2) return null;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            } else {
                map.put(nums[i], i);
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int[] a = {2, 7, 11, 15};
        int target = 13;
        int[] res = s.twoSum(a, target);
        System.out.println(res[0] + " " + res[1]);
    }
}
