/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 模板方法模式 - 模板
 */
public abstract class AbstractDisplay {

    protected abstract void open();

    protected abstract void print();

    protected abstract void close();

    public void display() {
        open();
        for (int i = 0; i < 5; i++) {
            print();
        }
        close();
    }

}
