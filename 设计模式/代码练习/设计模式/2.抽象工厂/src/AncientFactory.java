public class AncientFactory extends AbstractFactory {
    @Override
    public Weapon createWeapon() {
        return new Sword();
    }

    @Override
    public Vehicle createVehicle() {
        return new Horse();
    }
}
