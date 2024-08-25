import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solution_387 {

    /*public int firstUniqChar(String s) {
        // map 用来存储每个字符出现的次数
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int count = map.getOrDefault(s.charAt(i), 0) + 1;
            map.put(s.charAt(i), count);
        }
        for (int j = 0; j < s.length(); j++) {
            if (map.get(s.charAt(j)) == 1) {
                return j;
            }
        }
        return -1;
    }*/

    /* 改进：
    我们可以对方法一进行修改，使得第二次遍历的对象从字符串变为哈希映射。
    */
    public int firstUniqChar(String s) {
        Map<Character, Integer> position = new HashMap<Character, Integer>();
        int n = s.length();
        /*当我们第一次遍历字符串时，设当前遍历到的字符为 c。
        如果 c 不在哈希映射中，我们就将 c 与它的索引作为一个键值对加入哈希映射中。
        否则我们将 c 在哈希映射中对应的值修改为 −1。*/
        for (int i = 0; i < n; ++i) {
            char ch = s.charAt(i);
            if (position.containsKey(ch)) {
                position.put(ch, -1);
            } else {
                position.put(ch, i);
            }
        }
        /*在第一次遍历结束后，我们只需要再遍历一次哈希映射中的所有值。
        找出其中不为 -1 的最小值，即为第一个不重复字符的索引。
        如果哈希映射中的所有值均为 -1，我们就返回 −1。*/
        int first = n;
        /* entrySet():
        返回映射中包含的映射的 Set 视图。
        Set 视图意思是 HashMap 中所有的键值对都被看作是一个 set 集合。
        可以与 for-each 循环一起使用，用来遍历迭代 HashMap 中每一个映射项。*/
        for (Map.Entry<Character, Integer> entry : position.entrySet()) {
            int pos = entry.getValue();
            if (pos != -1 && pos < first) {
                first = pos;
            }
        }
        if (first == n) {
            first = -1;
        }
        return first;
    }

    public static void main(String[] args) {
        String str1 = "leetcode"; // 0
        String str2 = "loveleetcode"; // 2
        Solution_387 s = new Solution_387();
        int res = s.firstUniqChar(str1);
        System.out.println(res);
    }
}
