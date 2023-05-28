/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public boolean isNumber(String s) {
        char[] chars = s.toCharArray();

        boolean addMinFlag = false;
        boolean pointFlag = false;
        boolean eFlag = false;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(c == ' '){
                i++;
            }else if(c == '+' || c == '-'){
                if(addMinFlag){
                    return false;
                }
                addMinFlag = true;
            }else if((c-'a' >= 0 && c-'a' <= 26) || (c-'A' >= 0 && c-'A' <= 26)){

            }else if(c == '.'){
                if(pointFlag){
                    return true;
                }
                pointFlag = true;
            }else if(c == 'e' || c == 'E'){
                if(eFlag){
                    return false;
                }
                eFlag = true;

            }
        }
        return true;
    }
}
