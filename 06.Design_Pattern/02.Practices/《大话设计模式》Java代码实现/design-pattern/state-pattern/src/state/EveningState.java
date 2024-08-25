package state;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description:
 */
public class EveningState extends State {
    private final String name;

    public EveningState(String name) {
        this.name = name;
    }

    @Override
    public void handle(DevWork work) {
        System.out.println("state: " + name);
        if (work.getCurTime() < 21) {
            System.out.println("time: " + work.getCurTime() + ", 卷狗，还搁着加班呢");
            return;
        }
        System.out.println("time: " + work.getCurTime() + ", 该润了");
        try {
            work.setCurState(WorkStateMap.getState("morning"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
