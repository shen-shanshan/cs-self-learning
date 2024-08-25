public class Solution_88 {

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;
        int j = n - 1;
        int len = nums1.length;
        while (i >= 0 && j >= 0) {
            if (nums1[i] >= nums2[j]) {
                nums1[len - 1] = nums1[i];
                i--;
            } else {
                nums1[len - 1] = nums2[j];
                j--;
            }
            len--;
        }
        if (j >= 0) {
            for (int k = 0; k <= j; k++) {
                nums1[k] = nums2[k];
            }
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int m = 3;
        int[] nums2 = {2, 5, 6};
        int n = 3;

        int[] nums3 = {4, 5, 6, 0};
        int m2 = 3;
        int[] nums4 = {1};
        int n2 = 1;

        Solution_88 s = new Solution_88();
        s.merge(nums3, m2, nums4, n2);
        for (int i : nums3) {
            System.out.print(i + " ");
        }
    }
}
