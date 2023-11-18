package state;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 下午状态
 */
public class AfternoonState extends State {
    private final String name;

    public AfternoonState(String name) {
        this.name = name;
    }

    @Override
    public void handle(DevWork work) {
        System.out.println("state: " + name);
        if (work.getCurTime() < 18) {
            System.out.println("time: " + work.getCurTime() + ", 继续写代码");
            return;
        }
        System.out.println("time: " + work.getCurTime() + ", 该吃饭了");
        try {
            work.setCurState(WorkStateMap.getState("evening"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
