// 自己写的递归，没过
public class Solution {
    public String decodeString(String s) {
        return decode(s, 0, s.length() - 1);
    }

    public String decode(String s,int start,int end){
        StringBuilder sb = new StringBuilder();
        int i = start;
        int num = 0;
        while(i < end) {
            // 判断字母
            char cur = s.charAt(i);
            if(cur-'a'>=0 && cur-'a'<26){
                sb.append(cur);
            }
            // 判断数字
            if(cur-'0'>=0 && cur-'0'<=9){
                num = cur-'0';
                while(cur != '['){
                    i++;
                    cur = s.charAt(i);
                    if(cur-'0'>=0 && cur-'0'<=9){
                        num = num*10 +(cur-'0');
                    }
                }
                // 此时遍历到 '['
                int begin = i+1;
                int flag = 1;
                while(flag > 0){
                    i++;
                    cur = s.charAt(i);
                    if(cur == '['){
                        flag++;
                    }else if(cur == ']'){
                        flag--;
                    }
                }
                // 此时遍历到 ']'
                sb.append(decode(s,begin,i));
            }
        }
        return sb.toString();
    }
}