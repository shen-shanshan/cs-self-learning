// Event（事件）
public class WakeUpEvent extends Event<Child> {
    long timeStamp;
    String loc;

    public WakeUpEvent(long timeStamp, String loc) {
        this.timeStamp = timeStamp;
        this.loc = loc;
    }

    Child source;

    @Override
    Child getSource() {
        return source;
    }
}
