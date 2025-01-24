public class Solution_278 {
    /* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */
    public int firstBadVersion(int n) {
        VersionControl vc = new VersionControl(2);
        // 二分查找
        int low = 1;
        int high = n;
        int mid;
        while (low < high) {
            mid = low + (high - low) / 2;
            if (!vc.isBadVersion(mid)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        // low == high 时跳出循环
        return low;
    }

    public static void main(String[] args) {
        int n = 3;
        // int bad = 4;

        Solution_278 s = new Solution_278();
        int res = s.firstBadVersion(n);
        System.out.println(res);
    }
}
