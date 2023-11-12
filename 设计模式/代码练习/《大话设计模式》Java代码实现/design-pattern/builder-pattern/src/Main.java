import builder.ConcretePersonABuilder;
import builder.ConcretePersonBBuilder;
import builder.PersonDirector;

public class Main {
    public static void main(String[] args) {
        PersonDirector personDirector = new PersonDirector();
        // 按顺序一创建对象A
        System.out.println(personDirector.construct1(new ConcretePersonABuilder()).toString());
        // 按顺序二创建对象B
        System.out.println(personDirector.construct2(new ConcretePersonBBuilder()).toString());
        // 总结：使用构建器模式，可以使对象的构建更加：
        //     1.简单（封装构建的过程）；
        //     2.可信（约束构建的内容）；
        //     3.灵活（定义构建的步骤）。
    }
}