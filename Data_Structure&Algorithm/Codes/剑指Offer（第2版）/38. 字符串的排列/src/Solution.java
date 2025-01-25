import java.util.*;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    List<String> res = new ArrayList<>();

    public String[] permutation(String s) {
        if (s.length() == 0) {
            return new String[]{};
        }
        char[] str = s.toCharArray();
        backTrack(str, 0);
        return res.toArray(new String[res.size()]);
    }

    public void backTrack(char[] str, int index) {
        // base case
        if (index == str.length - 1) {
            res.add(String.valueOf(str));
        }
        Set<Character> set = new HashSet<>();
        // 遍历所有情况
        for (int i = index; i < str.length; i++) {
            // 判断当前位置出现的字符是否重复
            if (!set.contains(str[i])) {
                set.add(str[i]);
                swap(str, index, i);
                // 向下递归
                backTrack(str, index + 1);
                // 回溯
                swap(str, index, i);
            }
        }
    }

    public void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
