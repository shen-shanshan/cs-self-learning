public class Student extends Person {
    public int num = 20;

    public void printNum() {
        int num = 30;
        System.out.println(num);
        System.out.println(this.num);
        System.out.println(super.num);
    }

    /*public Student() {
        super(name);
    }*/

    public Student(String name) {
        // 调用本类构造方法
        // this();
        super(name);
    }

    // 方法重写（覆盖）
    // 子类重写父类的方法时，访问权限不能更低（如：此处的 public 不能去掉）
    // 父类的静态方法，子类在重写时也必须是静态
    @Override
    public void printNumber(){
        super.printNumber();
        System.out.println(20);
    }
}
