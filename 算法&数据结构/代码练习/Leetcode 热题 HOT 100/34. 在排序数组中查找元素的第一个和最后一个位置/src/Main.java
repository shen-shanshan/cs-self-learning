public class Main {
    public static void main(String[] args) {
        int[] arr1 = {5, 7, 7, 8, 8, 10};
        int target1 = 8; // {3, 4}
        int target2 = 6; // {-1, -1}
        int[] arr2 = {};
        int target3 = 0; // {-1, -1}

        Solution s = new Solution();
        int[] ans1 = s.searchRange(arr1, target1);
        int[] ans2 = s.searchRange(arr1, target2);
        int[] ans3 = s.searchRange(arr2, target3);
        print(ans1);
        print(ans2);
        print(ans3);
    }

    public static void print(int[] a) {
        for (int i : a) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("---------------------");
    }
}
