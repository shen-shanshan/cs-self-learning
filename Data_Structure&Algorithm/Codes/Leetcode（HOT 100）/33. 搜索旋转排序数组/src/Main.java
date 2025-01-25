public class Main {
    public static void main(String[] args) {
        int[] arr1 = {4, 5, 6, 7, 0, 1, 2};
        int[] arr2 = {1,3};
        int target1 = 0; // 4
        int target2 = 3; // -1

        Solution s = new Solution();
        /*int ans1 = s.search(arr1, target1);
        int ans2 = s.search(arr1, target2);
        System.out.println(ans1);
        System.out.println(ans2);*/
        int ans = s.search(arr2,3);
        System.out.println(ans);
    }
}
