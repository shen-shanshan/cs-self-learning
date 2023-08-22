import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> ans = new ArrayList<>();
        // 将一个源单词的所有字母异位词表示为一个 key
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            // 将每一个单词转化为一个 int 类型的数
            // int 32位：xxxx .... xxzy x... .... dcba
            int value = 0;
            for (int j = 0; j < strs[i].length(); j++) {
                int k = strs[i].charAt(j) - 'a';
                value += 1 << k;
            }
            // 判断该单词组合是否已经存在
            if (!map.containsKey(value)) {
                // 第一次遇到某个组合
                List<String> cur = new ArrayList<>();
                cur.add(strs[i]);
                ans.add(cur);
                map.put(value, ans.size() - 1);
            } else {
                // 某个组合已经存在过
                int index = map.get(value);
                List<String> cur = ans.get(index);
                cur.add(strs[i]);
            }
        }
        return ans;
    }
}