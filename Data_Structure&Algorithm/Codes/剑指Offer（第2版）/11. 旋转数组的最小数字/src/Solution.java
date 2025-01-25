public class Solution {
    public int minArray(int[] numbers) {
        if (numbers.length == 1) {
            return numbers[0];
        }
        int i = 0;
        while (i < numbers.length - 1) {
            if (numbers[i] > numbers[i + 1]) {
                return numbers[i + 1];
            }
            i++;
        }
        // 当恰好旋转了 n 次，旋转后的数组与原数组相同，最小值为数组的第一个元素
        return numbers[0];
    }
}
