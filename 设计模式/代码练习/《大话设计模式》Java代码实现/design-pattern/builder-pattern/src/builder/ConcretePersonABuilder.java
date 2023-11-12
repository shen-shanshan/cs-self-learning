package builder;

import product.Person;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 具体建造者-A（定义对象的装配方式）
 */
public class ConcretePersonABuilder extends PersonBuilder {
    private final Person person;

    public ConcretePersonABuilder() {
        this.person = new Person();
    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public void setPersonName() {
        person.setName("申杉杉");
        System.out.println("设置姓名为：申杉杉");
    }

    @Override
    public void setPersonGender() {
        person.setGender("男");
        System.out.println("设置性别为：男");
    }

    @Override
    public void setPersonAddress() {
        person.setAddress("中国-西安");
        System.out.println("设置地址为：中国-西安");
    }
}
