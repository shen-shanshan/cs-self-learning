import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 1207.独一无二的出现次数
 */
public class Solution1207 {
    public boolean uniqueOccurrences(int[] arr) {
        int len = arr.length;

        // 统计每个数的出现次数
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }

        // 统计出现次数的种类
        Set<Integer> set = new HashSet<>();
        for (Map.Entry<Integer, Integer> x : map.entrySet()) {
            int freq = x.getValue();
            if (!set.contains(freq)) {
                set.add(freq);
            } else {
                return false;
            }
        }

        return true;
    }
}
