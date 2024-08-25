public class ModernFactory extends AbstractFactory {
    @Override
    public Weapon createWeapon() {
        return new Gun();
    }

    @Override
    public Vehicle createVehicle() {
        return new Car();
    }
}
