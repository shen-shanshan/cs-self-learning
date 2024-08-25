public class Main {
    public static void main(String[] args) {
        int[] arr1 = {2, 3, 1, 1, 4};
        int[] arr2 = {3, 2, 1, 0, 4};

        Solution s = new Solution();

        boolean ans1 = s.canJump(arr1);
        boolean ans2 = s.canJump(arr2);

        System.out.println(ans1);
        System.out.println(ans2);
    }
}
