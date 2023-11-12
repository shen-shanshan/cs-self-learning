package subject;

import observer.Observer;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 抽象主题类（可以被多个观察者订阅）
 */
public abstract class Subject {
    public abstract void addObserver(Observer observer);

    public abstract void delObserver(Observer observer);

    /**
     * @description: 通知所有的观察者
     * @author: SSS
     * @date: 2023/11/12 16:05
     **/
    public abstract void notifyObserver();

    public abstract String getName();

    public abstract String getStatus();

    public abstract void setStatus(String status);
}
