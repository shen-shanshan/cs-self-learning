import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        // if (n < 2) return null;
        List<List<Integer>> ans = new ArrayList<>();
        // 从小到大排序
        // 保证每一个三元组从左到右都是从小到大，防止统计重复
        Arrays.sort(nums);
        for (int first = 0; first < n; first++) {
            // 需要和上一次枚举的数不同
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            // 第三个元素的指针
            int third = n - 1;
            int target = -nums[first];
            for (int second = first + 1; second < n; second++) {
                // 需要和上一次枚举的数不同
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                // 需要保证 b 的指针在 c 的指针的左侧
                while (second < third && nums[second] + nums[third] > target) {
                    --third;
                }
                // 如果指针重合，随着 b 后续的增加
                // 就不会有满足 a+b+c=0 并且 b<c 的 c 了，可以退出循环
                if (second == third) {
                    break;
                }
                if (nums[second] + nums[third] == target) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[third]);
                    ans.add(list);
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        int[] nums2 = {0};

        Solution s = new Solution();
        List<List<Integer>> ans = s.threeSum(nums1);
        for (List<Integer> x : ans) {
            System.out.print("[");
            for (Integer y : x) {
                System.out.print(y + " ");
            }
            System.out.println("]");
        }
    }
}
