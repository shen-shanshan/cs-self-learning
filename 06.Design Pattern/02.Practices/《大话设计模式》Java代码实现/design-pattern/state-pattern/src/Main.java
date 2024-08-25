import state.DevWork;
import state.WorkStateMap;

public class Main {
    public static void main(String[] args) {
        DevWork work;
        try {
            work = new DevWork(WorkStateMap.getState("morning"), 8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 根据时间切换状态
        for (int i = 8; i < 24; i+=2) {
            work.changeState(i);
        }
    }
}