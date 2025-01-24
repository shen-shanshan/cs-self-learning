package memento;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 玩家需要被备份的属性
 */
public class PersonMemento {
    private int vitality; // 生命值
    private int attack; // 攻击力
    private int defense; // 防御力

    public PersonMemento() {
    }

    public PersonMemento(int vitality, int attack, int defense) {
        this.vitality = vitality;
        this.attack = attack;
        this.defense = defense;
    }

    public PersonMemento(PersonMemento memento) {
        this.vitality = memento.getVitality();
        this.attack = memento.getAttack();
        this.defense = memento.getDefense();
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
