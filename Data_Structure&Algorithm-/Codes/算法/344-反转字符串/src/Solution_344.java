public class Solution_344 {
    // 方法一：递归
    /*public void reverseString(char[] s) {
        reverse(s, 0, s.length - 1);
    }

    public void reverse(char[] s, int start, int end) {
        if (start >= end) return;
        char tmp = s[start];
        s[start] = s[end];
        s[end] = tmp;
        reverse(s, ++start, --end);
    }*/

    // 方法二：双指针
    public void reverseString(char[] s) {
        if (s.length == 0 || s.length == 1) return;
        int start = 0;
        int end = s.length - 1;
        while (start < end) {
            char tmp = s[start];
            s[start++] = s[end];
            s[end--] = tmp;
        }
    }

    public static void main(String[] args) {
        char[] s1 = {'h', 'e', 'l', 'l', '0'};
        char[] s2 = {'H', 'a', 'n', 'n', 'a', 'h'};

        Solution_344 s = new Solution_344();
        s.reverseString(s1);
        s.reverseString(s2);

        for (char x : s1) {
            System.out.print(x);
        }
        System.out.println();
        for (char x : s2) {
            System.out.print(x);
        }
    }
}
