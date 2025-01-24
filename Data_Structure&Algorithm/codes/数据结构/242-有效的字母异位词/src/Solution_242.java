import java.util.HashMap;

public class Solution_242 {

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int len = s.length();
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int count = map.getOrDefault(c, 0) + 1;
            map.put(c, count);
        }
        for (int j = 0; j < len; j++) {
            char c = t.charAt(j);
            int count = map.getOrDefault(c, 0);
            if (count > 0) {
                map.put(c, count - 1);
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s1 = "anagram";
        String t1 = "nagaram";

        String s2 = "rat";
        String t2 = "car";

        Solution_242 s = new Solution_242();
        boolean res = s.isAnagram(s2, t2);
        System.out.println(res);
    }
}
