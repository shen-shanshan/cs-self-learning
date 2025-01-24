/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-13
 * @Description: 原型模式 - 原型
 */
public interface Product extends Cloneable {

    public abstract void use(String s);

    public abstract Product createClone();

}
