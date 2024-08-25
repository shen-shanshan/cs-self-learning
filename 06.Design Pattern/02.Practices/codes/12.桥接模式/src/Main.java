public class Main {
    public static void main(String[] args) {
        Vehicle v = new RedVehicle(new Car());
        System.out.println(v.getColor() + "的" + ((RedVehicle) v).impl.getName()); // 红色的车
    }
}
