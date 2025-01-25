public class Outer2 {
    public int num = 10;
    class Inner {
        public int num = 20;
        public void show(){
            int num = 30;
            System.out.println(num);// 30
            // 这里的 this 指向本类（Inner）的对象
            System.out.println(this.num);// 20
            // 这里通过外部类对象来访问外部类的数据
            System.out.println(Outer2.this.num);// 10
        }
    }
}

