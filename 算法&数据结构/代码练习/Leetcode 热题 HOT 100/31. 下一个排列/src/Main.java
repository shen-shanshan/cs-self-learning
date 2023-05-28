public class Main {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3}; // {1, 3, 2}
        int[] arr2 = {2, 3, 1}; // {3, 1, 2}
        int[] arr3 = {3, 2, 1}; // {1, 2, 3}
        int[] arr4 = {1, 1, 5}; // {1, 5, 1}

        Solution s = new Solution();
        s.nextPermutation(arr1);
        s.nextPermutation(arr2);
        s.nextPermutation(arr3);
        s.nextPermutation(arr4);
        print(arr1);
        print(arr2);
        print(arr3);
        print(arr4);
    }

    public static void print(int[] nums) {
        int n = nums.length;
        System.out.print("{");
        for (int i = 0; i < n - 1; i++) {
            System.out.print(nums[i] + ", ");
        }
        System.out.println(nums[n - 1] + "}");
    }
}


