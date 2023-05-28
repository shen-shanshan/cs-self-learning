import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public String reverseWords(String s) {
        List<String> list = new ArrayList<>();
        char[] chars = s.toCharArray();
        int len = chars.length;
        if (len == 0) {
            return "";
        }
        int left = len - 1;
        int right = len - 1;
        while (left >= 0) {
            // left 指针先走，找到每一个单词的末尾
            while (left >= 0 && chars[left] == ' ') {
                left--;
            }
            if (left < 0) {
                break;
            }
            right = left;
            // 找到当前单词的开头
            while (left >= 0 && chars[left] != ' ') {
                left--;
            }
            String cur = s.substring(left + 1, right + 1);
            list.add(cur);
        }
        // 全空字符串："   "
        if (list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i) + " ");
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }

}
