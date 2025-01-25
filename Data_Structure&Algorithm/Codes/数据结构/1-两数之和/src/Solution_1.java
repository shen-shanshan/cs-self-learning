import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution_1 {

    public int[] twoSum(int[] nums, int target) {
        /*使用哈希表，可以将寻找 target - x的时间复杂度降低到从 O(N)降低到 O(1)。
        我们创建一个哈希表，对于每一个 x，我们首先查询哈希表中是否存在 target - x。
        然后将 x插入到哈希表中，即可保证不会让 x和自己匹配。*/
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            if (hashtable.containsKey(target - nums[i])) {
                return new int[]{hashtable.get(target - nums[i]), i};
            }
            hashtable.put(nums[i], i);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        Solution_1 s = new Solution_1();
        int[] res = s.twoSum(nums, target);
        for (int i : res) {
            System.out.println(i);
        }
    }
}
