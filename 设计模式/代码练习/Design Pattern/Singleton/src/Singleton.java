/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-13
 * @Description: 单例模式
 */
public class Singleton {

    private static volatile Singleton instance = null;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
