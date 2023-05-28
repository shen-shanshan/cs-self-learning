import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[] arr1 = {1,2,3};
        int[] arr2 = {0,1};
        int[] arr3 = {1};

        Solution s = new Solution();
        List<List<Integer>> ans1 = s.permute(arr1);
        List<List<Integer>> ans2 = s.permute(arr2);
        List<List<Integer>> ans3 = s.permute(arr3);

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
