public class Main {
    public static void main(String[] args) {
        int[] arr1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int[] arr2 = {1};
        int[] arr3 = {5, 4, -1, 7, 8};

        Solution s = new Solution();

        int ans1 = s.maxSubArray(arr1);
        int ans2 = s.maxSubArray(arr2);
        int ans3 = s.maxSubArray(arr3);

        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println(ans3);
    }
}
