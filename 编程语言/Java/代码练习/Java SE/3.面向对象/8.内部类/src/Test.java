public class Test {
    public static void main(String[] args) {
        // 间接访问内部类的成员
        Outer o = new Outer();
        o.method();

        // 直接访问内部类的成员
        Outer.Inner oi = new Outer().new Inner();
        oi.show();

        // 访问静态内部类的成员
        // 方式一：
        Outer.Inner2 oi2 = new Outer.Inner2();
        oi2.show();
        // 方式二：
        Outer.Inner2.show();
        System.out.println("----------");

        Outer2.Inner oi3 = new Outer2().new Inner();
        oi3.show();
        System.out.println("----------");

        Outer3 o3 = new Outer3();
        o3.method();
        System.out.println("----------");

        InterImpl it = new InterImpl();
        it.method();
        System.out.println("----------");

        // 创建的是接口的子类（实现类）的对象
        Inter i = new Inter() {
            @Override
            public void show() {
                System.out.println("show2.1");
            }

            @Override
            public void show2() {
                System.out.println("show2.2");
            }
        };
        i.show();
        i.show2();
    }
}
