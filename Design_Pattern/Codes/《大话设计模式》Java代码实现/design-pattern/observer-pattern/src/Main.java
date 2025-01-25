import observer.ConcreteObserver;
import subject.ConcreteSubject;
import subject.Subject;

public class Main {
    public static void main(String[] args) {
        // set up
        Subject subject = new ConcreteSubject("宝宝", "睡着了");
        subject.addObserver(new ConcreteObserver("爸爸", "做饭", subject));
        subject.addObserver(new ConcreteObserver("妈妈", "喂奶", subject));
        // notify
        subject.setStatus("哭了");
        subject.notifyObserver();
    }
}