public class InterImpl {
    public void method() {
        // 匿名内部类（局部位置）
        // 本质上是一个继承了 Inter 类或者实现了 Inter 接口的子类匿名对象
        new Inter() {
            // 必须重写接口的所有抽象方法
            public void show() {
                System.out.println("show");
            }

            public void show2() {
                System.out.println("show2");
            }
        }.show();
    }
}
