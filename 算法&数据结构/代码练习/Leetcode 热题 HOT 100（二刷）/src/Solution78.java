import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 78.子集
 */
public class Solution78 {

    List<List<Integer>> ans = new ArrayList<>();

    List<Integer> path = new ArrayList<>();

    public List<List<Integer>> subsets(int[] nums) {
        backtrack(nums, 0);
        return ans;
    }

    public void backtrack(int[] nums, int index) {
        if (index == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }

        // 不拿当前数字
        backtrack(nums, index + 1);

        // 拿当前数字
        path.add(nums[index]);
        backtrack(nums, index + 1);
        path.remove(path.size() - 1);
    }
}
