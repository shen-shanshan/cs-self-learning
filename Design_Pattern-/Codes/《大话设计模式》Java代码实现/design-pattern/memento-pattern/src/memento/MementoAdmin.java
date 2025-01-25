package memento;

import java.util.*;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 管理备份的类
 */
public class MementoAdmin {
    // 1.不同玩家应该有各自的备份，以name进行区分，使用map进行映射
    // 2.同一个玩家应该有多个备份，使用栈进行存储
    private static final Map<String, Stack<PersonMemento>> mementoMap = new HashMap<>();

    /**
     * @description: 保存备份
     * @author: SSS
     * @date: 2023/11/18 16:31
     * @param: 玩家的名称和状态
     **/
    public static void saveMemento(String name, PersonMemento memento) {
        if (!mementoMap.containsKey(name)) {
            mementoMap.put(name, new Stack<>());
        }
        Stack<PersonMemento> mementos = mementoMap.getOrDefault(name, new Stack<>());
        mementos.push(memento);
        mementoMap.put(name, mementos);
    }

    /**
     * @description: 恢复备份
     * @author: SSS
     * @date: 2023/11/18 16:46
     * @param: 玩家的名称
     * @return: 备份数据
     **/
    public static PersonMemento getMemento(String name) throws Exception {
        if (!mementoMap.containsKey(name)) {
            throw new Exception("玩家不存在！");
        }
        Stack<PersonMemento> mementos = mementoMap.getOrDefault(name, new Stack<>());
        PersonMemento res = null;
        if (!mementos.isEmpty()) {
            res = mementos.pop();
        }
        return Optional.ofNullable(res).orElse(new PersonMemento(100, 10, 10));
    }
}
