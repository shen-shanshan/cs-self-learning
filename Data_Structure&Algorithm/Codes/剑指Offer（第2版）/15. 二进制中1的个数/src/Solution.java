public class Solution {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int i = 31;
        int count = 0;
        while (i >= 0) {
            if ((n & (1 << i)) != 0) {
                count++;
            }
            i--;
        }
        return count;
    }
}
