import java.util.Arrays;

// 鸽笼问题
public class Solution {
    /*public int findRepeatNumber(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int index = nums[i];
            if (index < 0) {
                index += nums.length;
            }
            if (nums[index] < 0) {
                return index;
            }
            nums[index] = nums[index] - nums.length;
        }
        return -1;
    }*/
    public int findRepeatNumber(int[] nums) {
        if (nums.length == 0) {
            return -1;
        }
        int i = 0;
        for (i = 0; i < nums.length; i++) {
            while (nums[i] != i) {
                if(nums[i] == nums[nums[i]]){
                    return nums[i];
                }
                int a = nums[i];
                nums[i] = nums[a];
                nums[a] = a;
            }
        }
        return -1;
    }
}
