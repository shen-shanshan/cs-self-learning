package observer;

import subject.Subject;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 具体观察者
 */
public class ConcreteObserver extends Observer {
    private final String name;
    private final String action;
    private final Subject subject;

    public ConcreteObserver(String name, String action, Subject subject) {
        this.name = name;
        this.action = action;
        this.subject = subject;
    }

    @Override
    public void update() {
        System.out.println(subject.getName() + subject.getStatus() + "，" + name + action);
    }
}
