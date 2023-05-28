public class RedVehicle extends Vehicle {
    public VehicleImpl impl;

    public RedVehicle(VehicleImpl impl) {
        this.impl = impl;
    }

    @Override
    public String getColor() {
        return "红色";
    }
}
