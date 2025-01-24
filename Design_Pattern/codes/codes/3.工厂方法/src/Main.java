public class Main {
    public static void main(String[] ags) {
        // 多态
        Vehicle v = new CarFactory().create();
        v.getName(); // this is a car
    }
}
