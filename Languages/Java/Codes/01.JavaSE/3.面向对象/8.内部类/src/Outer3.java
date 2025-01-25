public class Outer3 {
    private int num = 10;
    public void method(){
        // 局部变量（必须用 final 修饰）
        final int num2 = 20;
        // 内部类（局部位置）
        class Inner{
            public void show(){
                System.out.println(num);
                System.out.println(num2);
            }
        }
        // 在局部位置创建内部类对象，并调用内部类的方法
        Inner i = new Inner();
        i.show();
    }
}

