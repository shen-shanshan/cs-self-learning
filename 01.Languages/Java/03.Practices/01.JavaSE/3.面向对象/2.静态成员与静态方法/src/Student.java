public class Student {
    public static int age;
    // String name;
    // java: 无法从静态上下文中引用非静态 变量 this
    static String name;

    public Student() {
    }

    public Student(int age) {
        this.age = age;
    }

    // 静态方法只能访问静态的成员变量、方法
    /*public static void setName(String name) {
        name = name;
    }*/
}
