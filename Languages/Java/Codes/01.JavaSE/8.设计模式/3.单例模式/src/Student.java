// 单例模式：确保类在内存中只有一个对象，该实例必须自动创建，且对外提供。
// 饿汉式：类一加载就创建对象，不会出问题，一般开发时使用。
public class Student {
    // 构造方法私有，不让外界创建对象
    private Student() {
    }

    // 自己创建对象
    public static Student s = new Student();

    // 外界无法创建对象，只能通过类名调用静态方法来获取该类的对象
    public static Student getStudent() {
        return s;
    }
}
