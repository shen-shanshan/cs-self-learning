import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        // Map<key,value>
        Map<String, Integer> map = new HashMap<>();
        map.put("何信义", 10);
        map.put("薛寅珊", 20);
        map.put("吕晓克", 30);

        // 遍历
        // 方法一：获取所有键值的集合
        Set<String> set = map.keySet();
        for (String x : set) {
            int value = map.get(x);
            System.out.println(value);
        }
        System.out.println("----------");

        // 方法二：获取所有键值对对象的集合
        Set<Map.Entry<String, Integer>> set2 = map.entrySet();
        for (Map.Entry<String, Integer> x : set2) {
            String name = x.getKey();
            int age = x.getValue();
            System.out.println(name + "今年" + age + "岁了！！");
        }
    }
}
