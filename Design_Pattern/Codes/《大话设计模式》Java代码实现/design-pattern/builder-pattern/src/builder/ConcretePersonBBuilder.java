package builder;

import product.Person;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 具体建造者-B（定义对象的装配方式）
 */
public class ConcretePersonBBuilder extends PersonBuilder {
    private final Person person;

    public ConcretePersonBBuilder() {
        this.person = new Person();
    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public void setPersonName() {
        person.setName("小明");
        System.out.println("设置姓名为：小明");
    }

    @Override
    public void setPersonGender() {
        person.setGender("男");
        System.out.println("设置性别为：男");
    }

    @Override
    public void setPersonAddress() {
        person.setAddress("中国-北京");
        System.out.println("设置地址为：中国-北京");
    }
}
