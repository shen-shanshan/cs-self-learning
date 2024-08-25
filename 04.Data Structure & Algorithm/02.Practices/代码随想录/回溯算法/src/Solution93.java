import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 93.复原 IP 地址
 */
public class Solution93 {

    List<String> ans = new ArrayList<>();

    List<String> path = new ArrayList<>();

    public List<String> restoreIpAddresses(String s) {
        backtrack(s, 0);
        return ans;
    }

    public void backtrack(String s, int index) {
        // 剪枝
        if (path.size() > 4) {
            return;
        }

        // 记录结果
        if (index == s.length()) {
            if (path.size() < 4) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String x : path) {
                sb.append(x + ".");
            }
            sb.deleteCharAt(sb.length() - 1);
            ans.add(sb.toString());
            return;
        }

        if (s.charAt(index) == '0') {
            // 前导 0 的情况
            path.add("0");
            backtrack(s, index + 1);
            path.remove(path.size() - 1);
        } else {
            for (int i = index; i < Math.min(index + 3, s.length()); i++) {
                String cur = s.substring(index, i + 1);
                int val = Integer.parseInt(cur);
                if (val <= 255) {
                    path.add(cur);
                    backtrack(s, i + 1);
                    path.remove(path.size() - 1);
                }
            }
        }
    }
}
