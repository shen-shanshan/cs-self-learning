public class AnimalTools {
    // 构造私有，防止外界创建该类的对象
    private AnimalTools(){}

    // 多态提高了代码的维护性
    public static void use(Animal a){
        a.eat();
        a.sleep();
    }
}

