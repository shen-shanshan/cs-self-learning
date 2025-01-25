public class Solution_189 {
    // 方法一：使用额外的数组
    /*public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; ++i) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);
    }*/

    // 方法二：环状替换
    /*public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        int count = gcd(k, n);
        for (int start = 0; start < count; ++start) {
            int current = start;
            int prev = nums[start];
            do {
                int next = (current + k) % n;
                int temp = nums[next];
                nums[next] = prev;
                prev = temp;
                current = next;
            } while (start != current);
        }
    }

    public int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }*/

    // 方法三：数组翻转
    public void rotate(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start += 1;
            end -= 1;
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 4, 5, 6, 7};
        int k1 = 3;
        int[] nums2 = {-1, -100, 3, 99};
        int k2 = 2;
        int[] nums3 = {1};
        int k3 = 0;
        //int[] nums3 = {-1};
        //int k3 = 2;
        int[] nums4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
        int k4 = 38;

        Solution_189 s = new Solution_189();
        s.rotate(nums1, k1);
        s.rotate(nums2, k2);
        s.rotate(nums3, k3);
        s.rotate(nums4, k4);

        for (int x : nums1) {
            System.out.print(x + " ");
        }
        System.out.println();
        for (int x : nums2) {
            System.out.print(x + " ");
        }
        System.out.println();
        for (int x : nums3) {
            System.out.print(x + " ");
        }
        System.out.println();
        for (int x : nums4) {
            System.out.print(x + " ");
        }
    }
}
