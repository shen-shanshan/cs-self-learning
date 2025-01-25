/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public String reverseLeftWords(String s, int n) {
        String str1 = s.substring(0, n);
        String str2 = s.substring(n);
        StringBuilder sb = new StringBuilder();
        sb.append(str2).append(str1);
        return sb.toString();
    }

}
