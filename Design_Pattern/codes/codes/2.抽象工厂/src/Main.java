import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class Main {
    public static void main(String[] args) {
        AbstractFactory f = new ModernFactory();
        Weapon w = f.createWeapon();
        Vehicle v = f.createVehicle();
        w.printName(); // gun
        v.printName(); // car

        // 切换另一个工厂
        f = new AncientFactory();
        // 生产的是另一系列的产品
        w = f.createWeapon();
        v = f.createVehicle();
        w.printName(); // sword
        v.printName(); // horse
    }
}
