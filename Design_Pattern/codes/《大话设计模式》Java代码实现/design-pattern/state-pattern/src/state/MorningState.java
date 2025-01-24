package state;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 上午状态
 */
public class MorningState extends State {
    private final String name;

    public MorningState(String name) {
        this.name = name;
    }

    @Override
    public void handle(DevWork work) {
        System.out.println("state: " + name);
        if (work.getCurTime() < 12) {
            System.out.println("time: " + work.getCurTime() + ", 继续写代码");
            return;
        }
        System.out.println("time: " + work.getCurTime() + ", 该吃饭了");
        try {
            work.setCurState(WorkStateMap.getState("afternoon"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
