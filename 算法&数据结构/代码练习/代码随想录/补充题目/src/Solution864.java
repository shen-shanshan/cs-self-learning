import java.util.Objects;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 864.比较含退格的字符串
 */
public class Solution864 {
    public boolean backspaceCompare(String s, String t) {
        return analyse(s).equals(analyse(t));
    }

    public String analyse(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '#') {
                sb.append(c);
            } else if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        // System.out.println(sb.toString());

        return sb.toString();
    }
}
