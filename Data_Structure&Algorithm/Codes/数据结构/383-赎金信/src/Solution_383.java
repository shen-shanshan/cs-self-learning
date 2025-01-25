import java.util.HashMap;

public class Solution_383 {

    public boolean canConstruct(String ransomNote, String magazine) {
        if (ransomNote.length() > magazine.length()) {
            return false;
        }
        HashMap<Character, Integer> map = new HashMap<>();
        // 第一次遍历，统计杂志中出现的各个字符的次数
        for (int i = 0; i < magazine.length(); i++) {
            int count = map.getOrDefault(magazine.charAt(i), 0) + 1;
            map.put(magazine.charAt(i), count);
        }
        // 第二次遍历，检查赎金信字符串中出现的字符在 map 中是否存在。
        // 若存在，将对应的字符的次数减一；若不存在，则报错。
        for (int j = 0; j < ransomNote.length(); j++) {
            char c = ransomNote.charAt(j);
            if (map.containsKey(c) == true) {
                int count = map.get(c) - 1;
                if (count == 0) {
                    map.remove(c);
                } else {
                    map.put(c, count);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String ransomNote = "aa";
        String magazine = "ab";

        Solution_383 s = new Solution_383();
        boolean res = s.canConstruct(ransomNote, magazine);
        System.out.println(res);
    }
}
