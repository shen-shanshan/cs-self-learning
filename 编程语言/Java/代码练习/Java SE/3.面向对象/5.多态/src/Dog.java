public class Dog extends Animal {
    int age = 20;

    public void eat() {
        System.out.println("狗吃肉");
    }

    public void sleep() {
        System.out.println("狗睡觉");
    }

    public static void print(){
        System.out.println("子类静态方法");
    }

    public void print2(){
        System.out.println("狗的特有方法");
    }
}



