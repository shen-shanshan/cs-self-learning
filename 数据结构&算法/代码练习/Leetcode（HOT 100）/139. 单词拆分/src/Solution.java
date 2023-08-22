import java.util.HashMap;
import java.util.List;

public class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String i : wordDict) {
            map.put(i, 1);
        }
        int start = 0;
        int end = 0;
        while (end < s.length()) {
            String cur = s.substring(start, end + 1);
            if (!map.containsKey(cur)) {
                end++;
            } else {
                start = end + 1;
                end++;
            }
        }
        return end == start;
    }
}
