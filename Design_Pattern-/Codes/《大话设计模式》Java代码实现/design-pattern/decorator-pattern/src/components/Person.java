package components;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-29
 * @Description: 具体对象-人
 */
public class Person extends Component {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public void show() {
        System.out.println("我是：" + name);
    }
}
