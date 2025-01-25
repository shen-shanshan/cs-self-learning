public class Main {
    public static void main(String[] args) {
        int[][] arr1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] arr2 = {{1, 4}, {4, 5}};

        Solution s = new Solution();
        int[][] ans1 = s.merge(arr1);
        int[][] ans2 = s.merge(arr2);

        print(ans1);
        print(ans2);
    }

    public static void print(int[][] arr) {
        for (int[] i : arr) {
            System.out.print("{ ");
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println("}");
        }
        System.out.println("----------------");
    }
}
