/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-13
 * @Description: 原型模式
 */
public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        MessageBox messageBox = new MessageBox('#');
        manager.register("#", messageBox);
        Product p = manager.create("#");
        p.use("hello");
        // p.use("牛逼！！");
    }
}
