import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        Solution s = new Solution();
        List<List<Integer>> ans = s.subsets(nums);

        print(ans);
    }

    public static void print(List<List<Integer>> a) {
        for (List<Integer> i : a) {
            System.out.print("{ ");
            for (Integer j : i) {
                System.out.print(j + " ");
            }
            System.out.println("}");
        }
    }
}
