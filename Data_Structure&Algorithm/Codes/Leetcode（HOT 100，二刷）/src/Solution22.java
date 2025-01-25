import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 22.括号生成
 */
public class Solution22 {
    List<String> ans = new ArrayList<>();

    StringBuilder path = new StringBuilder();

    public List<String> generateParenthesis(int n) {
        if (n == 0) {
            return ans;
        }
        backtrack(n, 0, 0);
        return ans;
    }

    public void backtrack(int n, int left, int right) {
        if (left + right == 2 * n) {
            ans.add(path.toString());
            return;
        }

        if (left < n) {
            path.append('(');
            backtrack(n, left + 1, right);
            path.deleteCharAt(path.length() - 1);
        }

        if (right < n && right < left) {
            path.append(')');
            backtrack(n, left, right + 1);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
