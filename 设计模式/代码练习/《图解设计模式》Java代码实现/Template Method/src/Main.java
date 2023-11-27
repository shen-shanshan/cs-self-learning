/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 模板方法模式
 */
public class Main {
    public static void main(String[] args) {
        AbstractDisplay ad = new CharDisplay('s');
        ad.display();
        ad = new StringDisplay("hello");
        ad.display();
    }
}
