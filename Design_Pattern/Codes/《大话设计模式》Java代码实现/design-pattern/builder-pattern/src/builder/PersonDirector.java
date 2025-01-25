package builder;

import product.Person;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 指挥者（定义创建对象的步骤或算法）
 */
public class PersonDirector {
    public Person construct1(PersonBuilder personBuilder) {
        System.out.println("第一步：");
        personBuilder.setPersonName();
        System.out.println("第二步：");
        personBuilder.setPersonGender();
        System.out.println("第三步：");
        personBuilder.setPersonAddress();
        System.out.println("创建对象：");
        return personBuilder.getPerson();
    }

    public Person construct2(PersonBuilder personBuilder) {
        System.out.println("第一步：");
        personBuilder.setPersonAddress();
        System.out.println("第二步：");
        personBuilder.setPersonGender();
        System.out.println("第三步：");
        personBuilder.setPersonName();
        System.out.println("创建对象：");
        return personBuilder.getPerson();
    }
}
