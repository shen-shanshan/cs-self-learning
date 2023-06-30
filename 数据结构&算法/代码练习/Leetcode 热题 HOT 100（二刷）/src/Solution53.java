/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 53.最大子数组和
 */
public class Solution53 {
    public int maxSubArray(int[] nums) {
        int len = nums.length;

        int max = nums[0];
        int sum = nums[0];

        int i = 1;

        while(i < len){
            if(sum <= 0){
                sum = nums[i];
            }else{
                sum += nums[i];
            }
            max = Math.max(max,sum);
            i++;
        }

        return max;
    }
}
