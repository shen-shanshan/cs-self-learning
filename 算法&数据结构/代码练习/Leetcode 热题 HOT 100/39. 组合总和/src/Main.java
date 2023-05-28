import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[] arr1 = {2, 3, 6, 7};
        int[] arr2 = {2, 3, 5};
        int[] arr3 = {2};

        Solution s = new Solution();
        List<List<Integer>> ans1 = s.combinationSum(arr1, 7);
        List<List<Integer>> ans2 = s.combinationSum(arr2, 8);
        List<List<Integer>> ans3 = s.combinationSum(arr3, 1);
        print(ans1);
        print(ans2);
        print(ans3);
    }

    public static void print(List<List<Integer>> l) {
        for (List<Integer> i : l) {
            for (Integer j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("-----------");
    }
}
