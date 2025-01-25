import java.util.Random;

/*
* 随机化:
* 随机挑选一个下标，检查它是否是众数，如果是就返回，否则继续随机挑选。
* 期望的随机次数是常数。
* 每一次随机后，我们需要 O(n) 的时间判断这个数是否为众数，因此期望的时间复杂度为 O(n)。
* 空间复杂度：O(1)。随机方法只需要常数级别的额外空间。
* */
public class Solution {
    // 随机选取数组中的一个数
    private int randRange(Random rand, int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    // 统计被选中的数的出现次数
    private int countOccurences(int[] nums, int num) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == num) {
                count++;
            }
        }
        return count;
    }

    public int majorityElement(int[] nums) {
        Random rand = new Random();

        int majorityCount = nums.length / 2;

        while (true) {
            int candidate = nums[randRange(rand, 0, nums.length)];
            if (countOccurences(nums, candidate) > majorityCount) {
                return candidate;
            }
        }
    }
}