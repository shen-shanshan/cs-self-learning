import memento.MementoAdmin;
import memento.Person;
import memento.PersonMemento;

public class Main {
    public static void main(String[] args) {
        Person p = new Person("申杉杉", new PersonMemento(100, 10, 10));
        p.printState();

        while (p.fight()) {
            System.out.println("发生战斗...");
            p.printState();
            MementoAdmin.saveMemento(p.getName(), p.save());
        }

        System.out.println("玩家死亡");
        p.printState();

        System.out.println("恢复到死亡之前的状态");
        try {
            p.recover(MementoAdmin.getMemento(p.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.printState();
    }
}