package subject;

import observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 具体主题类
 */
public class ConcreteSubject extends Subject {
    private final String name;
    private String status;
    private final List<Observer> observers;

    public ConcreteSubject(String name, String status) {
        this.name = name;
        this.status = status;
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void delObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(Observer::update);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
