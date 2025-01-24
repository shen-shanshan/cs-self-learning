import java.util.ArrayList;
import java.util.List;

// Source（事件源）
public class Child {
    private boolean cry = false;

    private List<Observer> obs = new ArrayList<>();

    {
        obs.add(new Dad());
        obs.add(new Mom());
        obs.add(new Dog());
    }

    public boolean isCry() {
        return cry;
    }

    public void wakeUp() {
        cry = true;

        // 产生事件
        WakeUpEvent event = new WakeUpEvent(System.currentTimeMillis(), "bed");

        // 将事件传递给观察者处理
        for (Observer o : obs) {
            o.actionOnWakeUp(event);
        }
    }
}


