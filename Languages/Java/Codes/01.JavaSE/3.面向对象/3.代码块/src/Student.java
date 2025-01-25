public class Student {
    static int age;
    int number;

    // 构造代码块
    // 每次调用构造方法，都执行一次所有的构造代码块，并且先于构造方法执行
    {
        //age = 10;
    }

    // 静态代码块
    // 比构造代码块先执行，随着类的初始化，只执行一次
    static {
        age = 0;
    }

    // 构造方法
    public Student() {
    }
}
