public class Main {
    public static void main(String[] args) {
        int[] nums = {2, 0, 2, 1, 1, 0};

        Solution s = new Solution();
        s.sortColors(nums);

        print(nums);
    }

    public static void print(int[] a) {
        System.out.print("{ ");
        for (int i : a) {
            System.out.print(i + " ");
        }
        System.out.println("}");
    }
}
