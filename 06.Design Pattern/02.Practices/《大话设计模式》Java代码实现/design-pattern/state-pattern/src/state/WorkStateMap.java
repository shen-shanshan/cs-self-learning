package state;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 保存各个状态的实例（避免重复创建对象）
 */
public class WorkStateMap {
    private static final Map<String, State> map;

    static {
        map = new HashMap<>();
        map.put("morning", new MorningState("morning"));
        map.put("afternoon", new AfternoonState("afternoon"));
        map.put("evening", new EveningState("evening"));
    }

    public static State getState(String name) throws Exception {
        if (!map.containsKey(name)) {
            throw new Exception("状态不存在");
        }
        return map.get(name);
    }
}
