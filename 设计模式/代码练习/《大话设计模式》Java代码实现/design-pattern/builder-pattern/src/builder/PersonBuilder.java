package builder;

import product.Person;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 抽象建造者
 */
public abstract class PersonBuilder {
    public abstract Person getPerson();

    public abstract void setPersonName();

    public abstract void setPersonGender();

    public abstract void setPersonAddress();
}
