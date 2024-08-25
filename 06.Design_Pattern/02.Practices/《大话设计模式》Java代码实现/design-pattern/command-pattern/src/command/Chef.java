package command;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 厨师类（Receiver）-只负责做菜（接收命令）
 */
public class Chef {
    public void makeChicken() {
        System.out.println("烤鸡腿");
    }

    public void makeFish() {
        System.out.println("烤鱼");
    }
}
