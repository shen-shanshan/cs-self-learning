// Observers（观察者）
public class Observers {
}

class Dad implements Observer {
    @Override
    public void actionOnWakeUp(WakeUpEvent event) {
        System.out.println("摇婴儿床");
    }
}

class Mom implements Observer {
    @Override
    public void actionOnWakeUp(WakeUpEvent event) {
        System.out.println("喂奶");
    }
}

class Dog implements Observer {
    @Override
    public void actionOnWakeUp(WakeUpEvent event) {
        System.out.println("汪汪汪");
    }
}
