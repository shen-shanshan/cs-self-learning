package test;

import framework.Factory;
import framework.Product;
import idcard.IDCardFactory;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 工厂方法模式
 */
public class Main {
    public static void main(String[] args) {
        Factory factory = new IDCardFactory();
        Product card1 = factory.create("a", 1);
        Product card2 = factory.create("b", 2);
        Product card3 = factory.create("c", 3);
        card1.use();
        card2.use();
        card3.use();
        System.out.println(((IDCardFactory) factory).getOwners());
        System.out.println(((IDCardFactory) factory).getMap());
    }
}
