public class Test {
    public static void main(String[] args) {
        // java 只支持单继承，不支持多继承，但支持多层继承。
        // 子类不能继承父类的构造方法

        // 打印时，变量的取值就近
        Student s = new Student("何信义");
        s.printNum();
        System.out.println("----------");
        // 子类只能继承父类所有的非私有的成员（变量、方法）
        // System.out.println(s.age);

        System.out.println(s.name);
        System.out.println("----------");

        s.printNumber();

        // final关键字：
        // 修饰类，该类无法被继承。
        // 修饰方法，该方法无法被重写。
        // 修饰变量，该变量无法修改其值。
        // 修饰引用类型，无法改变其地址值。
        final Student s2 = new Student("傻逼");
        // s2 = new Student("hhh");// 重新分配内存空间
    }
}
