package idcard;

import framework.Factory;
import framework.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 工厂方法模式 - 具体工厂
 */
public class IDCardFactory extends Factory {

    // 保存已注册的用户
    private List<String> owners = new ArrayList<>();

    // 卡号<->用户
    private Map<Integer, String> map = new HashMap<>();

    public List<String> getOwners() {
        return owners;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    @Override
    protected Product createProduct(String owner, int number) {
        return new IDCard(owner, number);
    }

    @Override
    protected void registerProduct(Product p) {
        IDCard idCard = (IDCard) p;
        owners.add(idCard.getOwner());
        map.put(idCard.getNumber(), idCard.getOwner());
    }

}
