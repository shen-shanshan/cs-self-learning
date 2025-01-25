public class Solution {
    public int hammingDistance(int x, int y) {
        int count = 0;
        for (int i = 0; i < 31; i++) {
            int x1 = x >> i;
            int y1 = y >> i;
            if ((x1 & 1) != (y1 & 1)) {
                count++;
            }
        }
        return count;
    }
}