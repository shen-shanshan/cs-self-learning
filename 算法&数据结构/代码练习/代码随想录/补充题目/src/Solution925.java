/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 925.长按键入
 */
public class Solution925 {
    public boolean isLongPressedName(String name, String typed) {
        // 双指针
        int i = 0;
        int j = 0;

        while (j < typed.length()) {
            if (i < name.length() && name.charAt(i) == typed.charAt(j)) {
                // typed 与 name 匹配上了
                i++;
                j++;
            } else if (j > 0 && typed.charAt(j) == typed.charAt(j - 1)) {
                // typed 是长按键入的部分
                j++;
            } else {
                return false;
            }
        }

        return i == name.length();
    }
}
