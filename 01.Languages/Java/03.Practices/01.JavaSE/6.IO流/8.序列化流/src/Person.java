import java.io.Serializable;

// 类通过实现 Serializable 序列化接口来启用序列化功能。
// 该接口没有方法，是一个标记接口。
public class Person implements Serializable {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "Person[name=" + name + ",age=" + age + "]";
        //return name+"今年"+age+"岁了";
    }
}
