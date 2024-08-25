package memento;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 玩家类（需要被备份的类）
 */
public class Person {
    private final String name;
    private PersonMemento state;

    public Person(String name, PersonMemento memento) {
        this.name = name;
        this.state = memento;
    }

    public String getName() {
        return name;
    }

    public PersonMemento getState() {
        return state;
    }

    public void setState(PersonMemento state) {
        this.state = state;
    }

    public void printState() {
        System.out.println("name: " + name);
        System.out.println("vitality: " + state.getVitality());
        System.out.println("attack: " + state.getAttack());
        System.out.println("defense: " + state.getDefense());
    }

    public boolean fight() {
        state.setVitality(state.getVitality() - 40);
        state.setAttack(state.getAttack() + 20);
        state.setDefense(state.getDefense() + 20);
        // 玩家挂了
        if (state.getVitality() <= 0) {
            System.out.println("game over!");
            return false;
        }
        // 还活着
        return true;
    }

    /**
     * @description: 执行备份
     * @author: SSS
     * @date: 2023/11/18 16:22
     * @return: 当前状态的备份
     **/
    public PersonMemento save() {
        return new PersonMemento(state);
    }

    /**
     * @description: 恢复备份
     * @author: SSS
     * @date: 2023/11/18 16:23
     * @param: 之前备份的数据
     **/
    public void recover(PersonMemento memento) {
        this.state = memento;
    }
}
