public class Main {
    public static void main(String[] args) {
        char[][] board1 = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        char[][] board2 = {{'A'}};

        String word1 = "ABCCED"; // true
        String word2 = "SEE"; // true
        String word3 = "ABCB"; // false

        Solution s = new Solution();

        boolean ans1 = s.exist(board1, word1);
        boolean ans2 = s.exist(board1, word2);
        boolean ans3 = s.exist(board1, word3);

        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println(ans3);

        boolean ans4 = s.exist(board2, "A");
        System.out.println(ans4);
    }
}
