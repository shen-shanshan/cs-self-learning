public class Main {
    public static void main(String[] args) {
        int[] arr1 = {100, 4, 200, 1, 3, 2}; // 4
        int[] arr2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1}; // 9

        Solution s = new Solution();
        int ans1 = s.longestConsecutive(arr1);
        int ans2 = s.longestConsecutive(arr2);

        System.out.println(ans1);
        System.out.println(ans2);
    }
}
