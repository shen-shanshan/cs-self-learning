public class Solution_557 {
    public String reverseWords(String s) {
        char[] ch = s.toCharArray();
        int i = 0;
        for (int j = 0; j < ch.length; j++) {
            if (ch[j] == ' ') {
                reverse(ch, i, j - 1);
                i = j + 1;
            } else if (j == ch.length - 1) {
                reverse(ch, i, j);
            }
        }
        return String.valueOf(ch);
    }

    public void reverse(char[] s, int start, int end) {
        while (start < end) {
            char tmp = s[start];
            s[start++] = s[end];
            s[end--] = tmp;
        }
    }

    public static void main(String[] args) {
        // String str = "Let's take LeetCode contest";
        String str = " 1 1";

        Solution_557 s = new Solution_557();
        String res = s.reverseWords(str);
        System.out.println(res);
    }
}
