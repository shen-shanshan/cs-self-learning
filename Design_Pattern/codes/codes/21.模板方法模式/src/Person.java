public abstract class Person {
    abstract void getName();

    abstract void getAge();

    abstract void getSex();

    public void getInfo() {
        getName();
        getAge();
        getSex();
    }
}

class XiaoMing extends Person {

    @Override
    void getName() {
        System.out.println("小明");
    }

    @Override
    void getAge() {
        System.out.println(20);
    }

    @Override
    void getSex() {
        System.out.println("男");
    }
}
