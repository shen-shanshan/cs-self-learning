import java.util.HashMap;
import java.util.Map;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-13
 * @Description: 原型模式 - 使用者
 */
public class Manager {

    private Map<String, Product> showcase;

    public Manager() {
        showcase = new HashMap<>();
    }

    public void register(String name, Product proto) {
        showcase.put(name, proto);
    }

    public Product create(String name) {
        Product p = showcase.get(name);
        return p.createClone();
    }

}
