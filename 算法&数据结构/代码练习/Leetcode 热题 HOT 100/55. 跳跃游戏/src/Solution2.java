/*
 * 贪心算法：
 * 我们依次遍历数组中的每一个位置，并实时维护：最远可以到达的位置。
 * 对于当前遍历到的位置 x，如果它在最远可以到达的位置的范围内，那么我们就可以从起点通过若干次跳跃到达该位置。
 * 因此我们可以用 x + nums[x] 更新最远可以到达的位置。
 */
public class Solution2 {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        // 最远可以到达的位置
        int rightmost = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= rightmost) {
                // 更新最远可以到达的位置
                rightmost = Math.max(rightmost, i + nums[i]);
                // 如果最远可以到达的位置大于等于数组中的最后一个位置，那就说明最后一个位置可达
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        // 如果在遍历结束后，最后一个位置仍然不可达，我们就返回 False 作为答案。
        return false;
    }
}