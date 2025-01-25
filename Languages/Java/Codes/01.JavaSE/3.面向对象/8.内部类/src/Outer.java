public class Outer {
    private int num = 10;
    static int num2 = 20;

    // 内部类（成员位置）
    class Inner {
        public void show() {
            // 内部类可以访问外部类的成员（包括私有）
            System.out.println(num);
        }
    }

    static class Inner2 {
        public static void show() {
            // 静态内部类：只能访问被 static 修饰的外部类数据
            System.out.println(num2);
        }
    }

    public void method() {
        // 外部类要访问内部类的成员，必须先创建内部类的对象
        Inner i = new Inner();
        i.show();
    }
}

