public class Main {
    public static void main(String[] args) {
        int[][] grid = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};

        Solution s = new Solution();
        int ans = s.minPathSum(grid);
        System.out.println(ans); // 7
    }
}
