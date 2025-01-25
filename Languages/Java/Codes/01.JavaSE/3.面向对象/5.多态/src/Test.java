public class Test {
    // 多态：父类引用指向子类对象。
    public static void main(String[] args) {
        Animal a = new Dog();
        AnimalTools.use(a);
        System.out.println("----------");

        // 运行时，看的是父类的成员变量的值
        System.out.println(a.age);

        // 运行时，走的是父类的静态方法
        a.print();

        // 多态的弊端：不能使用子类的特有方法
        // a.print2();
        // 解决办法：
        // 转换引用类型（不创建新对象）。
        Dog d = (Dog) a;
        d.print2();
    }
}




