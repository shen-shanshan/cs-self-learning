package idcard;

import framework.Product;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 工厂方法模式 - 具体产品
 */
public class IDCard extends Product {

    private String owner;

    private int number;

    public IDCard(String owner, int number) {
        System.out.println("制作" + owner + "的ID卡");
        this.owner = owner;
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void use() {
        System.out.println("使用" + owner + "的ID卡");
    }

}
