import java.util.ArrayList;
import java.util.List;

public class Solution2 {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        backtrack(ans, n, 0, 0, new StringBuffer());
        return ans;
    }

    public void backtrack(List<String> ans, int n, int left, int right, StringBuffer cur) {
        // 递归结束条件
        if (left == n && right == n) {
            ans.add(cur.toString());
            return;
        }
        // 始终要保证 left >= right
        if (left == right) {
            cur.append('(');
            backtrack(ans, n, left + 1, right, cur);
            cur.deleteCharAt(cur.length() - 1);
        } else {
            if (left < n) {
                cur.append('(');
                backtrack(ans, n, left + 1, right, cur);
                cur.deleteCharAt(cur.length() - 1);
            }
            cur.append(')');
            backtrack(ans, n, left, right + 1, cur);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    public static void main(String[] args) {
        int n = 3;

        Solution s = new Solution();
        List<String> ans = s.generateParenthesis(n);
        for (String x : ans) {
            System.out.println(x);
        }
    }
}
