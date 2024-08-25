// 假设有一些观察婴儿是否睡醒的观察者
public interface Observer {
    void actionOnWakeUp(WakeUpEvent event);
}
