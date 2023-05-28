import java.util.ArrayList;
import java.util.List;

public class Solution_784 {
    List<String> res = new ArrayList<>();

    public List<String> letterCasePermutation(String S) {
        char[] chs = S.toCharArray();
        int n = chs.length;
        dfs(chs, n, 0);
        return res;
    }

    private void dfs(char[] chs, int n, int begin) {
        res.add(new String(chs));
        for (int i = begin; i < n; i++) {
            if (!Character.isDigit(chs[i])) {
                char tmp = chs[i];
                // 大小写交换（A：65，a：97）
                chs[i] = (char) (chs[i] - 'a' >= 0 ? chs[i] - 32 : chs[i] + 32);
                dfs(chs, n, i + 1);
                // 撤销操作，回溯
                chs[i] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        String s1 = "a1b2";
        // String s2 = "3z4";
        // String s3 = "12345";

        Solution_784 s = new Solution_784();
        List<String> res1 = s.letterCasePermutation(s1);
        // List<String> res2 = s.letterCasePermutation(s2);
        // List<String> res3 = s.letterCasePermutation(s3);
        for (String x : res1) {
            System.out.print(x + " ");
        }
    }
}
